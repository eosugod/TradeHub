package com.eosugod.tradehub.product.service;

import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;
import com.eosugod.tradehub.product.dto.response.ResponseProductDto;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.product.mapper.ProductMapper;
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
    void testCreateProduct() {
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

    @Test
    @DisplayName("상품 삭제 성공")
    void testDeleteProduct() {
        // given
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // when
        productService.deleteProduct(product.getId());

        // then
        verify(productRepository).delete(product);
    }

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
}
