package com.eosugod.tradehub.product.service;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.request.RequestUpdateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.repository.ProductRepository;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
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

    // 상품 수정
    @Transactional
    public ResponseProductDto updateProduct(Long productId, RequestUpdateProductDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        Product savedProduct = ProductMapper.toEntity(requestDto, product);
        return ProductMapper.toResponseDto(savedProduct);
    }

    // 상품 삭제
    @Transactional
    public boolean deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
        return true;
    }

    // 단일 상품 조회
    @Transactional(readOnly = true)
    public ResponseProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        return ProductMapper.toResponseDto(product);
    }

    // 전체 상품 조회
    @Transactional(readOnly = true)
    public Page<ResponseProductDto> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductMapper::toResponseDto);
    }
}
