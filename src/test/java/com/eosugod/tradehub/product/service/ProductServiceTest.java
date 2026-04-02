package com.eosugod.tradehub.product.service;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.request.RequestUpdateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.port.ProductPort;
import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import com.eosugod.tradehub.util.ExpectedException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
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

        given(productPort.save(any(ProductDomain.class))).willReturn(expect);

        // when
        ResponseProductDto responseDto = productService.createProduct(dto);

        // then
        assertNotNull(responseDto);
        assertEquals(expect.getSellerId(), responseDto.getSellerId());
        assertEquals(expect.getPrice().getValue(), responseDto.getPrice());
        assertEquals(expect.getTitle(), responseDto.getTitle());
        assertEquals(expect.getText(), responseDto.getText());
        assertEquals(expect.getLocationCode().getValue(), responseDto.getLocationCode());
        assertEquals(expect.getState(), responseDto.getState());
        assertEquals(expect.getThumbNailImage(), responseDto.getThumbNailImage());

        // Verify
        verify(productPort, times(1)).save(any(ProductDomain.class));
    }

    @Test
    @DisplayName("상품 수정")
    void testUpdateProduct() {
        // given
        Long productId = 1L;

        ProductDomain productDomain = ProductDomain.builder()
                .id(productId)
                .sellerId(2L)
                .price(new Money(BigDecimal.valueOf(1000)))
                .title("origin title")
                .text("origin text")
                .locationCode(new Address("01234567"))
                .state(Product.SaleState.FOR_SALE)
                .thumbNailImage("test_url")
                .build();

        RequestUpdateProductDto dto = RequestUpdateProductDto.builder()
                .price(BigDecimal.valueOf(2000))
                .title("updated title")
                .text("updated text")
                .locationCode("98765432")
                .thumbNailImage("updated_url")
                .build();

        ProductDomain expect = ProductDomain.builder()
                .id(productId)
                .sellerId(2L)
                .price(new Money(BigDecimal.valueOf(2000)))
                .title("updated title")
                .text("updated text")
                .locationCode(new Address("98765432"))
                .state(Product.SaleState.FOR_SALE)
                .thumbNailImage("updated_url")
                .build();

        given(productPort.findById(productId)).willReturn(Optional.of(productDomain));
        given(productPort.save(any(ProductDomain.class))).willReturn(expect);

        // when
        ResponseProductDto responseDto = productService.updateProduct(productId, dto);

        // then
        assertNotNull(responseDto);
        assertEquals(expect.getId(), responseDto.getId());
        assertEquals(expect.getPrice().getValue(), responseDto.getPrice());
        assertEquals(expect.getTitle(), responseDto.getTitle());
        assertEquals(expect.getText(), responseDto.getText());
        assertEquals(expect.getLocationCode().getValue(), responseDto.getLocationCode());
        assertEquals(expect.getState(), responseDto.getState());
        assertEquals(expect.getThumbNailImage(), responseDto.getThumbNailImage());

        // Verify
        verify(productPort, times(1)).findById(productId);
        verify(productPort, times(1)).save(any(ProductDomain.class));
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProduct_Success() {
        // given
        Long productId = 1L;
        ProductDomain productDomain = ProductDomain.builder()
                                                   .id(productId)
                                                   .sellerId(2L)
                                                   .price(new Money(BigDecimal.valueOf(1000)))
                                                   .title("Sample Product")
                                                   .text("This is a sample product")
                                                   .locationCode(new Address("01234567"))
                                                   .state(Product.SaleState.FOR_SALE)
                                                   .thumbNailImage("image_url")
                                                   .build();

        given(productPort.findById(productId)).willReturn(Optional.of(productDomain));

        // when
        boolean result = productService.deleteProduct(productId);

        // then
        assertTrue(result);
        verify(productPort, times(1)).findById(productId);
        verify(productPort, times(1)).delete(productDomain);
    }

    @Test
    @DisplayName("상품 삭제 실패")
    void testDeleteNotFound() {
        // given
        Long productId = 9L;
        given(productPort.findById(productId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ExpectedException.class, () -> productService.deleteProduct(productId));
    }

    @Test
    @DisplayName("단일 상품 조회")
    void testGetProduct() {
        // given
        Long productId = 1L;

        ProductDomain productDomain = ProductDomain.builder()
                                                   .id(productId)
                                                   .sellerId(2L)
                                                   .price(new Money(BigDecimal.valueOf(1000)))
                                                   .title("sample title")
                                                   .text("sample text")
                                                   .locationCode(new Address("0123456789"))
                                                   .state(Product.SaleState.FOR_SALE)
                                                   .thumbNailImage("test_url")
                                                   .build();

        given(productPort.findById(productId)).willReturn(Optional.of(productDomain));

        // when
        ResponseProductDto responseDto = productService.getProductById(productId);

        // then
        assertNotNull(responseDto);
        assertEquals(productDomain.getId(), responseDto.getId());
        assertEquals(productDomain.getTitle(), responseDto.getTitle());
        assertEquals(productDomain.getPrice().getValue(), responseDto.getPrice());
        assertEquals(productDomain.getLocationCode().getValue(), responseDto.getLocationCode());
        assertEquals(productDomain.getState(), responseDto.getState());

        verify(productPort, times(1)).findById(productId);
    }

    @Test
    @DisplayName("모든 상품 조회")
    void testGetAllProducts() {
        // given
        int page = 0;
        int size = 2;
        Pageable pageable = PageRequest.of(page, size);

        List<ProductDomain> productDomains = List.of(
                ProductDomain.builder()
                             .id(1L)
                             .sellerId(2L)
                             .price(new Money(BigDecimal.valueOf(1000)))
                             .title("Product 1")
                             .text("This is product 1")
                             .locationCode(new Address("0123456789"))
                             .state(Product.SaleState.FOR_SALE)
                             .thumbNailImage("image1_url")
                             .build(),
                ProductDomain.builder()
                             .id(2L)
                             .sellerId(3L)
                             .price(new Money(BigDecimal.valueOf(2000)))
                             .title("Product 2")
                             .text("This is product 2")
                             .locationCode(new Address("9876543210"))
                             .state(Product.SaleState.SOLD_OUT)
                             .thumbNailImage("image2_url")
                             .build()
        );

        Page<ProductDomain> productDomainPage = new PageImpl<>(productDomains, pageable, productDomains.size());
        given(productPort.findAll(pageable)).willReturn(productDomainPage);

        // when
        Page<ResponseProductDto> responseDtos = productService.getAllProducts(page, size);

        // then
        assertNotNull(responseDtos);
        assertEquals(productDomains.size(), responseDtos.getTotalElements());

        for (int i = 0; i < productDomains.size(); i++) {
            ProductDomain productDomain = productDomains.get(i);
            ResponseProductDto responseDto = responseDtos.getContent().get(i);

            assertEquals(productDomain.getId(), responseDto.getId());
            assertEquals(productDomain.getTitle(), responseDto.getTitle());
            assertEquals(productDomain.getPrice().getValue(), responseDto.getPrice());
            assertEquals(productDomain.getLocationCode().getValue(), responseDto.getLocationCode());
            assertEquals(productDomain.getState(), responseDto.getState());
        }

        verify(productPort, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("상품 검색 성공 테스트")
    void testSearchProducts() {
        // given
        String keyword = "Sample";
        Pageable pageable = PageRequest.of(0, 10);

        List<ProductDomain> productDomains = List.of(
                ProductDomain.builder()
                             .id(1L)
                             .sellerId(2L)
                             .price(new Money(BigDecimal.valueOf(1000)))
                             .title("Product 1")
                             .text("This is sample product 1")
                             .locationCode(new Address("0123456789"))
                             .state(Product.SaleState.FOR_SALE)
                             .thumbNailImage("image1_url")
                             .build(),
                ProductDomain.builder()
                             .id(2L)
                             .sellerId(3L)
                             .price(new Money(BigDecimal.valueOf(2000)))
                             .title("Product 2")
                             .text("This is sample product 2")
                             .locationCode(new Address("9876543210"))
                             .state(Product.SaleState.SOLD_OUT)
                             .thumbNailImage("image2_url")
                             .build()
        );

        Page<ProductDomain> productDomainPage = new PageImpl<>(productDomains, pageable, productDomains.size());

        given(productPort.searchByKeyword(keyword, pageable)).willReturn(productDomainPage);

        // when
        Page<ResponseProductDto> result = productService.searchProducts(keyword, 0, 10);

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals("Product 1", result.getContent().get(0).getTitle());
        assertEquals("Product 2", result.getContent().get(1).getTitle());
        verify(productPort, times(1)).searchByKeyword(keyword, pageable);
    }
}
