package com.eosugod.tradehub.product.service;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.request.RequestUpdateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.port.ProductPort;
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
    private final ProductPort productPort;

    // 상품 생성
    public ResponseProductDto createProduct(RequestCreateProductDto dto) {
        Product product = ProductMapper.dtoToPersistence(dto);
        ProductDomain productDomain = ProductMapper.persistenceToDomain(product);
        return ProductMapper.domainToDto(productPort.save(productDomain));
    }

    // 상품 수정
    @Transactional
    public ResponseProductDto updateProduct(Long id, RequestUpdateProductDto dto) {
        ProductDomain productDomain = productPort.findById(id)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        ProductDomain updatedProductDomain = productDomain.update(dto);
        return ProductMapper.domainToDto(productPort.save(updatedProductDomain));
    }

    // 상품 삭제
    @Transactional
    public boolean deleteProduct(Long productId) {
        ProductDomain productDomain = productPort.findById(productId)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        productPort.delete(productDomain);
        return true;
    }

    // 단일 상품 조회
    @Transactional(readOnly = true)
    public ResponseProductDto getProductById(Long id) {
        ProductDomain productDomain = productPort.findById(id)
                .orElseThrow(() -> new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND));
        return ProductMapper.domainToDto(productDomain);
    }

    // 전체 상품 조회
    @Transactional(readOnly = true)
    public Page<ResponseProductDto> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> productDomainPage = productPort.findAll(pageable);
        return productDomainPage.map(ProductMapper::domainToDto);
    }

    // 검색 상품 조회
    @Transactional(readOnly = true)
    public Page<ResponseProductDto> searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDomain> productDomainPage = productPort.searchByKeyword(keyword, pageable);
        return productDomainPage.map(ProductMapper::domainToDto);
    }
}
