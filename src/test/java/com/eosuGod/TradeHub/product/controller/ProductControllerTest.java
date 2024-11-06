package com.eosuGod.TradeHub.product.controller;

import com.eosuGod.TradeHub.product.dto.request.RequestCreateProductDto;
import com.eosuGod.TradeHub.product.dto.response.ResponseProductDto;
import com.eosuGod.TradeHub.product.entity.Product;
import com.eosuGod.TradeHub.product.service.ProductService;
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
