package com.eosugod.tradehub.product.adapter;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.port.ProductPort;
import com.eosugod.tradehub.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public void delete(ProductDomain productDomain) {
        Product product = ProductMapper.domainToPersistence(productDomain);
        productRepository.delete(product);
    }

    @Override
    public Optional<ProductDomain> findById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::persistenceToDomain);
    }

    @Override
    public Page<ProductDomain> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::persistenceToDomain);
    }
}
