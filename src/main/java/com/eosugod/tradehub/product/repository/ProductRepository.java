package com.eosugod.tradehub.product.repository;

import com.eosugod.tradehub.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.text) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> findByTitleOrText(@Param("keyword") String keyword, Pageable pageable);
}
