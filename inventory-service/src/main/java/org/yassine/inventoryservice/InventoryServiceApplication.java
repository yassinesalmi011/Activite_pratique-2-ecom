package org.yassine.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.yassine.inventoryservice.entities.Product;
import org.yassine.inventoryservice.repositories.ProductRepository;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(new Product(null, "Computer", 1200, 12));
            productRepository.save(new Product(null, "Printer", 300, 5));
            productRepository.save(new Product(null, "Smartphone", 800, 20));
            productRepository.findAll().forEach(p -> {
                System.out.println(p.toString());
            });
        };
    }

    @Bean
    org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer repositoryRestConfigurer() {
        return org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
                .withConfig((config, cors) -> config.exposeIdsFor(Product.class));
    }
}
