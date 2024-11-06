package com.eosuGod.TradeHub.product.controller;


import com.eosuGod.TradeHub.product.dto.request.RequestCreateProductDto;
import com.eosuGod.TradeHub.product.dto.response.ResponseProductDto;
import com.eosuGod.TradeHub.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
