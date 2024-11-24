package com.eosugod.tradehub.product.adapter;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.port.ProductPort;
import com.eosugod.tradehub.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductJpaAdapter implements ProductPort {
    private final ProductRepository productRepository;

    @Override
    public ProductDomain save(RequestCreateProductDto dto) {
        return ProductMapper.persistenceToDomain(productRepository.save(ProductMapper.dtoToPersistence(dto)));
    }
}
