package com.eosugod.tradehub.product.adapter;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.port.ProductPort;
import com.eosugod.tradehub.product.repository.ProductRepository;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductJpaAdapter implements ProductPort {
    private final ProductRepository productRepository;

    @Override
    public ProductDomain save(ProductDomain productDomain) {
        Product product = ProductMapper.domainToPersistence(productDomain);
        return ProductMapper.persistenceToDomain(productRepository.save(product));
    }

    @Override
    public ProductDomain findById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::persistenceToDomain)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
    }
}
