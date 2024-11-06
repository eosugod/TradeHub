package com.eosuGod.TradeHub.product.service;

import com.eosuGod.TradeHub.product.dto.request.RequestCreateProductDto;
import com.eosuGod.TradeHub.product.dto.response.ResponseProductDto;
import com.eosuGod.TradeHub.product.entity.Product;
import com.eosuGod.TradeHub.product.mapper.ProductMapper;
import com.eosuGod.TradeHub.product.repository.ProductRepository;
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
