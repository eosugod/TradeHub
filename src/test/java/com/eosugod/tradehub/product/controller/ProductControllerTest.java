package com.eosugod.tradehub.product.controller;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.service.ProductService;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

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

//    @Test
//    @DisplayName("모든 상품 조회 요청 성공")
//    void testGetAllProducts() throws Exception {
//        // given
//        List<ResponseProductDto> responseDtos = IntStream.range(0, 20)
//                                                         .mapToObj(i -> new ResponseProductDto((long) i, 1L, null,
//                                                                 BigDecimal.valueOf(1000 + i),
//                                                                 "Product " + i, "Description " + i,
//                                                                 "1234567890", Product.SaleState.FOR_SALE,
//                                                                 "image_url_" + i))
//                                                         .toList();
//        Page<ResponseProductDto> responsePage = new PageImpl<>(responseDtos, PageRequest.of(0, 20), 100);
//        given(productService.getAllProducts(0, 20)).willReturn(responsePage);
//
//        // when & then
//        mockMvc.perform(get("/products")
//                       .param("page", "0")
//                       .param("size", "20")
//                       .contentType(MediaType.APPLICATION_JSON))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.content.length()").value(20)) // 페이지당 20개
//               .andExpect(jsonPath("$.totalElements").value(100)) // 총 100개 데이터
//               .andExpect(jsonPath("$.content[0].title").value("Product 0"));
//    }
//
//    @Test
//    @DisplayName("키워드 검색 요청")
//    void testSearchProducts() throws Exception {
//        // given
//        String keyword = "sample";
//        List<ResponseProductDto> responseDtos = IntStream.range(0, 20)
//                                                         .mapToObj(i -> new ResponseProductDto((long) i, 1L, null,
//                                                                 BigDecimal.valueOf(1000 + i),
//                                                                 "Sample Product " + i, "Description " + i,
//                                                                 "1234567890", Product.SaleState.FOR_SALE,
//                                                                 "image_url_" + i))
//                                                         .toList();
//        Page<ResponseProductDto> responsePage = new PageImpl<>(responseDtos, PageRequest.of(0, 20), 50);
//        given(productService.searchProducts(keyword, 0, 20)).willReturn(responsePage);
//
//        // when & then
//        mockMvc.perform(get("/products/search")
//                       .param("keyword", keyword)
//                       .param("page", "0")
//                       .param("size", "20")
//                       .contentType(MediaType.APPLICATION_JSON))
//               .andExpect(status().isOk())
//               .andExpect(jsonPath("$.content.length()").value(20)) // 페이지당 20개
//               .andExpect(jsonPath("$.totalElements").value(50)) // 총 50개 데이터
//               .andExpect(jsonPath("$.content[0].title", Matchers.containsStringIgnoringCase("sample")));
//    }
}
