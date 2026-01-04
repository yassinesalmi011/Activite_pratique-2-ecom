package org.yassine.billingservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.yassine.billingservice.model.Product;

@FeignClient(name = "INVENTORY-SERVICE")
public interface ProductRestClient {
    @GetMapping("/products/{id}")
    Product findProductById(@PathVariable String id);

    @GetMapping("/products")
    java.util.Map<String, Object> allProducts();
}
