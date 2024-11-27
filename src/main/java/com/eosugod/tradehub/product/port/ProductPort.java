package com.eosugod.tradehub.product.port;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductPort {
    ProductDomain save(ProductDomain productDomain);
    void delete(ProductDomain productDomain);
    Optional<ProductDomain> findById(Long id);
    Page<ProductDomain> findAll(Pageable pageable);
}
