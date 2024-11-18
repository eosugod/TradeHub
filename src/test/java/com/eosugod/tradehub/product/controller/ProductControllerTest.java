package com.eosugod.tradehub.product.controller;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.service.ProductService;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 요청")
    void testCreateProduct() throws Exception {
        // given
        final RequestCreateProductDto dto = new RequestCreateProductDto(1L, BigDecimal.valueOf(1000), "create product", "sample text", "1234567890", Product.SaleState.FOR_SALE, "test_url");

        // when
        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(dto))
                                         .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());

        // then
        then(productService).should().createProduct(eq(dto));
    }

    @Test
    @DisplayName("상품 삭제 요청 성공")
    void testDeleteProduct() throws Exception {
        // given
        Long productId = 1L;

        // when
        Mockito.when(productService.deleteProduct(productId)).thenReturn(true);

        // then
        mockMvc.perform(delete("/products/{id}", productId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 삭제 요청 실패")
    void testDeleteNotFound() throws Exception {
        // given
        Long productId = 9L;

        // when
        Mockito.doThrow(new ExpectedException(ExceptionCode.PRODUCT_NOT_FOUND))
                .when(productService).deleteProduct(productId);

        // then
        mockMvc.perform(delete("/products/{id}", productId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("모든 상품 조회 요청 성공")
    void testGetAllProducts() throws Exception {
        // given
        List<ResponseProductDto> reponseDtos = List.of(
                new ResponseProductDto(1L, 1L, null, BigDecimal.valueOf(1000), "Product 1", "text 1", "1234567890", Product.SaleState.FOR_SALE, "image_url_1"),
                new ResponseProductDto(2L, 2L, null, BigDecimal.valueOf(2000), "Product 2", "text 2", "9876543210", Product.SaleState.FOR_SALE, "image_url_2")
        );
        given(productService.getAllProducts()).willReturn(reponseDtos);

        // when & then
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].title").value("Product 2"));
    }
}
