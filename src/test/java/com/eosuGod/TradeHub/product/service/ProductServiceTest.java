package com.eosugod.tradehub.product.service;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.repository.ProductRepository;
import com.eosugod.tradehub.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private RequestCreateProductDto requestDto;
    private ResponseProductDto responseDto;
    private Product product;

    @BeforeEach
    void setUp() {
        requestDto = new RequestCreateProductDto();
        requestDto.setSellerId(1L);
        requestDto.setPrice(BigDecimal.valueOf(1000));
        requestDto.setTitle("Sample Product");
        requestDto.setText("This is a sample product");
        requestDto.setLocationCode("0123456789");
        requestDto.setState(Product.SaleState.FOR_SALE);
        requestDto.setThumbNailImage("image_url");

        product = ProductMapper.toEntity(requestDto);
        responseDto = ProductMapper.toResponseDto(product);
    }

    @Test
    @DisplayName("상품 등록")
    public void testCreateProduct() {
        // given
        given(productRepository.save(any(Product.class))).willReturn(product);

        // when
        ResponseProductDto responseDto = productService.createProduct(this.requestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(product.getId(), responseDto.getId());
        assertEquals(product.getSellerId(), responseDto.getSellerId());
        assertEquals(product.getPrice().getValue(), responseDto.getPrice());
        assertEquals(product.getTitle(), responseDto.getTitle());
        assertEquals(product.getText(), responseDto.getText());
        assertEquals(product.getLocationCode().getValue(), responseDto.getLocationCode());
        assertEquals(product.getState(), responseDto.getState());
        assertEquals(product.getThumbNailImage(), responseDto.getThumbNailImage());
    }
}
