package com.eosugod.tradehub.product.controller;


import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.request.RequestUpdateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable Long productId,
                                                            @RequestBody @Valid RequestUpdateProductDto requestDto) {
        ResponseProductDto updatedProduct = productService.updateProduct(productId, requestDto);
        return ResponseEntity.ok(updatedProduct);
    }
}
