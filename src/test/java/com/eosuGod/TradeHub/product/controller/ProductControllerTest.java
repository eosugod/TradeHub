package com.eosugod.tradehub.product.controller;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    ProductController productController;

    @Autowired
    private MockMvc mockMvc;

    private RequestCreateProductDto requestDto;
    private ResponseProductDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        requestDto = new RequestCreateProductDto();
        requestDto.setSellerId(1L);
        requestDto.setTitle("Sample Product");
        requestDto.setText("This is a sample product");
        requestDto.setState(Product.SaleState.FOR_SALE);
        requestDto.setThumbNailImage("image_url");
        requestDto.setPrice(BigDecimal.valueOf(1000));
        requestDto.setLocationCode("1234567890");

        responseDto = new ResponseProductDto(
                1L,
                requestDto.getSellerId(),
                null,
                requestDto.getPrice(),
                requestDto.getTitle(),
                requestDto.getText(),
                requestDto.getLocationCode(),
                requestDto.getState(),
                requestDto.getThumbNailImage()
        );
    }

    @Test
    @DisplayName("상품 등록 요청")
    public void testCreateProduct() throws Exception {
        // given
        given(productService.createProduct(any(RequestCreateProductDto.class))).willReturn(responseDto);

        // when then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.sellerId").value(responseDto.getSellerId()))
                .andExpect(jsonPath("$.buyerId").value(responseDto.getBuyerId()))
                .andExpect(jsonPath("$.price").value(responseDto.getPrice()))
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.text").value(responseDto.getText()))
                .andExpect(jsonPath("$.locationCode").value(responseDto.getLocationCode()))
                .andExpect(jsonPath("$.state").value(responseDto.getState().toString()))
                .andExpect(jsonPath("$.thumbNailImage").value(responseDto.getThumbNailImage()));
    }

}
