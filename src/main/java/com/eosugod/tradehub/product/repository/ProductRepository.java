package com.eosugod.tradehub.product.repository;

import com.eosugod.tradehub.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
