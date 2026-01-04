package org.yassine.billingservice.web;

import org.springframework.web.bind.annotation.*;
import org.yassine.billingservice.entities.Bill;
import org.yassine.billingservice.entities.ProductItem;
import org.yassine.billingservice.repository.BillRepository;
import org.yassine.billingservice.repository.ProductItemRepository;
import org.yassine.billingservice.feign.CustomerRestClient;
import org.yassine.billingservice.feign.ProductRestClient;

import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillRestController {
    private final BillRepository billRepository;
    private final ProductItemRepository productItemRepository;
    private final CustomerRestClient customerRestClient;
    private final ProductRestClient productRestClient;

    public BillRestController(BillRepository billRepository,
            ProductItemRepository productItemRepository,
            CustomerRestClient customerRestClient,
            ProductRestClient productRestClient) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }

    // 🔹 Récupérer toutes les factures
    @GetMapping
    public List<Bill> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        bills.forEach(bill -> {
            try {
                java.util.Map<String, Object> cData = customerRestClient.findCustomerById(bill.getCustomerId());
                if (cData != null) {
                    org.yassine.billingservice.model.Customer customer = new org.yassine.billingservice.model.Customer();
                    Object idObj = cData.get("id");
                    if (idObj instanceof Number) {
                        customer.setId(((Number) idObj).longValue());
                    }
                    customer.setName((String) cData.get("name"));
                    customer.setEmail((String) cData.get("email"));
                    bill.setCustomer(customer);
                }
            } catch (Exception e) {
                System.err.println("Error fetching customer for bill " + bill.getId() + ": " + e.getMessage());
            }

            bill.getProductItems().forEach(pi -> {
                try {
                    pi.setProduct(productRestClient.findProductById(pi.getProductId()));
                } catch (Exception e) {
                    System.err.println("Error fetching product " + pi.getProductId() + ": " + e.getMessage());
                }
            });
        });
        return bills;
    }

    // 🔹 Récupérer une facture par ID
    @GetMapping("/{id}")
    public Bill getBill(@PathVariable Long id) {
        Bill bill = billRepository.findById(id).orElseThrow();
        try {
            java.util.Map<String, Object> cData = customerRestClient.findCustomerById(bill.getCustomerId());
            if (cData != null) {
                org.yassine.billingservice.model.Customer customer = new org.yassine.billingservice.model.Customer();
                Object idObj = cData.get("id");
                if (idObj instanceof Number) {
                    customer.setId(((Number) idObj).longValue());
                }
                customer.setName((String) cData.get("name"));
                customer.setEmail((String) cData.get("email"));
                bill.setCustomer(customer);
            }
        } catch (Exception e) {
            System.err.println("Error fetching customer for bill " + bill.getId() + ": " + e.getMessage());
        }

        bill.getProductItems().forEach(pi -> {
            try {
                pi.setProduct(productRestClient.findProductById(pi.getProductId()));
            } catch (Exception e) {
                System.err.println("Error fetching product " + pi.getProductId() + ": " + e.getMessage());
            }
        });
        return bill;
    }
}
