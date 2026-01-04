package org.yassine.billingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yassine.billingservice.entities.ProductItem;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {}

