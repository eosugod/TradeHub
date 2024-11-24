package com.eosugod.tradehub.product.service;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.product.port.ProductPort;
import com.eosugod.tradehub.product.repository.ProductRepository;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import com.eosugod.tradehub.util.ExpectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductPort productPort;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록")
    void testCreateProduct() {
        // given
        RequestCreateProductDto dto = RequestCreateProductDto.builder()
                .sellerId(2L)
                .price(BigDecimal.valueOf(1000))
                .title("Sample Product")
                .text("This is a sample product")
                .locationCode("01234567")
                .state(Product.SaleState.FOR_SALE)
                .thumbNailImage("image_url")
                .build();

        ProductDomain expect = ProductDomain.builder()
                .id(1L)
                .sellerId(2L)
                .price(new Money(BigDecimal.valueOf(1000)))
                .title("Sample Product")
                .text("This is a sample product")
                .locationCode(new Address("0123456789"))
                .state(Product.SaleState.FOR_SALE)
                .thumbNailImage("image_url")
                .build();

        given(productPort.save(dto)).willReturn(expect);

        // when
        productService.createProduct(dto);

        // then
        assertNotNull(dto);
        assertEquals(dto.getSellerId(), expect.getSellerId());
        assertEquals(dto.getPrice(), expect.getPrice().getValue());
        assertEquals(dto.getTitle(), expect.getTitle());
        assertEquals(dto.getText(), expect.getText());
        assertEquals(dto.getLocationCode(), expect.getLocationCode().getValue());
        assertEquals(dto.getState(), expect.getState());
        assertEquals(dto.getThumbNailImage(), expect.getThumbNailImage());

        verify(productPort, times(1)).save(any());
    }

//    @Test
//    @DisplayName("상품 삭제 성공")
//    void testDeleteProduct() {
//        // given
//        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
//
//        // when
//        productService.deleteProduct(product.getId());
//
//        // then
//        verify(productRepository).delete(product);
//    }

    @Test
    @DisplayName("상품 삭제 실패")
    void testDeleteNotFound() {
        // given
        Long productId = 9L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ExpectedException.class, () -> productService.deleteProduct(productId));
    }

    @Test
    @DisplayName("모든 상품 조회 성공")
    void testGetAllProducts() {
        // given
        List<Product> products = IntStream.range(0, 20)
                                          .mapToObj(i -> new Product((long) i, 1L, null, new Money(BigDecimal.valueOf(1000 + i)),
                                                  "Product " + i, "Description " + i,
                                                  new Address("1234567890"), Product.SaleState.FOR_SALE,
                                                  "image_url_" + i))
                                          .toList();

        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 20), 100);
        given(productRepository.findAll(any(Pageable.class))).willReturn(productPage);

        // when
        Page<ResponseProductDto> result = productService.getAllProducts(0, 20);

        // then
        assertThat(result.getTotalElements()).isEqualTo(100); // 총 100개
        assertThat(result.getContent().size()).isEqualTo(20); // 한 페이지에 20개
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Product 0");
        then(productRepository).should().findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("상품 검색 조회")
    void testSearchProducts() {
        // given
        String keyword = "sample";
        List<Product> products = IntStream.range(0, 20)
                                          .mapToObj(i -> new Product((long) i, 1L, null, new Money(BigDecimal.valueOf(1000 + i)),
                                                  "Sample Product " + i, "Description " + i,
                                                  new Address("1234567890"), Product.SaleState.FOR_SALE,
                                                  "image_url_" + i))
                                          .toList();

        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 20), 50);
        given(productRepository.findByTitleOrText(eq(keyword), any(Pageable.class)))
                .willReturn(productPage);

        // when
        Page<ResponseProductDto> result = productService.searchProducts(keyword, 0, 20);

        // then
        assertThat(result.getTotalElements()).isEqualTo(50); // total 50개
        assertThat(result.getContent().size()).isEqualTo(20); // page당 20개
        assertThat(result.getContent().get(0).getTitle()).contains("Sample");
        assertThat(result.getContent().get(0).getPrice()).isNotNull();
    }

    @Test
    @DisplayName("키워드 없는 상품 검색 조회")
    void testSearchProductsNoKeyword() {
        // given
        Page<Product> emptyPage = Page.empty(PageRequest.of(0, 20));
        given(productRepository.findByTitleOrText(eq(""), any(Pageable.class))).willReturn(emptyPage);

        // when
        Page<ResponseProductDto> result = productService.searchProducts("", 0, 20);

        // then
        assertThat(result.getTotalElements()).isEqualTo(0); // 결과 없음
        assertThat(result.getContent().size()).isEqualTo(0); // 페이지 데이터 없음
    }
}
