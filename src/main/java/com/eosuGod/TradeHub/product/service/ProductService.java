package com.eosugod.tradehub.product.service;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 상품 생성
    public ResponseProductDto createProduct(RequestCreateProductDto requestDto) {
        Product product = ProductMapper.toEntity(requestDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.toResponseDto(savedProduct);
    }
}
