package com.eosugod.tradehub.product.repository;

import com.eosugod.tradehub.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByTitleOrText(String titleKeyword, String textKeyword, Pageable pageable);
}
