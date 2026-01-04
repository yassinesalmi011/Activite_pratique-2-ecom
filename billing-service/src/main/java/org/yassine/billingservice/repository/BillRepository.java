package org.yassine.billingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yassine.billingservice.entities.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {}
