package com.eosugod.tradehub.product.controller;


import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.request.RequestUpdateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 상품 생성
    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Valid @RequestBody RequestCreateProductDto requestDto) {
        ResponseProductDto createdProduct = productService.createProduct(requestDto);
        return ResponseEntity.ok(createdProduct);
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable Long productId,
                                                            @RequestBody @Valid RequestUpdateProductDto requestDto) {
        ResponseProductDto updatedProduct = productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    // 단일 상품 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ResponseProductDto> getProduct(@PathVariable Long productId) {
        ResponseProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

    // 전체 상품 조회
    @GetMapping
    public ResponseEntity<Page<ResponseProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ResponseProductDto> responseDtos = productService.getAllProducts(page, size);
        return ResponseEntity.ok(responseDtos);
    }

    // 검색 상품 조회
    @GetMapping("/search")
    public ResponseEntity<Page<ResponseProductDto>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ResponseProductDto> responseDtos = productService.searchProducts(keyword, page, size);
        return ResponseEntity.ok(responseDtos);
    }
}
