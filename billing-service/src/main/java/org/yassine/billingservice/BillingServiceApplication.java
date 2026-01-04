package org.yassine.billingservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.yassine.billingservice.entities.ProductItem;
import org.yassine.billingservice.feign.CustomerRestClient;
import org.yassine.billingservice.model.Customer;
import org.yassine.billingservice.model.Product;
import org.yassine.billingservice.repository.BillRepository;
import org.yassine.billingservice.repository.ProductItemRepository;

import org.yassine.billingservice.entities.*;
import org.yassine.billingservice.feign.*;

import java.util.Date;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    public feign.codec.Decoder feignDecoder(com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        return new feign.jackson.JacksonDecoder(objectMapper);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository,
            ProductItemRepository productItemRepository,
            CustomerRestClient customerRestClient,
            ProductRestClient productRestClient) {
        return args -> {
            try {
                // Wait for services to be up and discovered
                Thread.sleep(10000); // Increased to 10s to be safe

                java.util.Map<String, Object> customerData = customerRestClient.findCustomerById(1L);
                System.out.println("DEBUG CUSTOMER: " + customerData);

                // Manually map customer data
                Customer customer = new Customer();
                // Handle Long/Integer conversion safely
                Object idObj = customerData.get("id");
                if (idObj instanceof Integer) {
                    customer.setId(((Integer) idObj).longValue());
                } else if (idObj instanceof Long) {
                    customer.setId((Long) idObj);
                }
                customer.setName((String) customerData.get("name"));
                customer.setEmail((String) customerData.get("email"));

                Bill bill = Bill.builder()
                        .billingDate(new Date())
                        .customerId(customer.getId())
                        .customer(customer)
                        .build();
                billRepository.save(bill);

                java.util.Map<String, Object> productsResponse = productRestClient.allProducts();
                System.out.println("DEBUG RESPONSE: " + productsResponse);

                java.util.Map<String, Object> embedded = (java.util.Map<String, Object>) productsResponse
                        .get("_embedded");
                System.out.println("DEBUG EMBEDDED: " + embedded);

                java.util.List<java.util.Map<String, Object>> productsList = (java.util.List<java.util.Map<String, Object>>) embedded
                        .get("products");
                System.out.println("DEBUG LIST: " + productsList);

                java.util.Map<String, Object> productData = productsList.get(0);
                String productId = (String) productData.get("id");
                System.out.println("DEBUG ID: " + productId);

                // Fetch full product details using ID (optional, but consistent) or use map
                // data
                Product product = productRestClient.findProductById(productId);
                System.out.println("DEBUG PRODUCT: " + product);

                ProductItem productItem = ProductItem.builder()
                        .productId(product.getId())
                        .price(product.getPrice())
                        .quantity(3)
                        .bill(bill)
                        .product(product)
                        .build();
                productItemRepository.save(productItem);
                System.out.println("DEBUG SUCCESS: Saved Bill and ProductItem");
            } catch (Exception e) {
                System.err.println("Error initializing data: " + e.getMessage());
                e.printStackTrace();
            }

        };
    }
}
