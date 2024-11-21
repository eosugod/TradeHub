package com.eosugod.tradehub.reservation.controller;

import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.reservation.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReservationController.class)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 생성 요청")
    void testCreateReservation() throws Exception {
        // given
        Long productId = 1L;
        RequestCreateReservationDto requestDto = RequestCreateReservationDto.builder()
                                                                            .productId(productId)
                                                                            .buyerId(2L)
                                                                            .price(BigDecimal.valueOf(10000))
                                                                            .locationCode("1234567890")
                                                                            .confirmedAt(LocalDateTime.now().plusHours(1))
                                                                            .build();

        ResponseReservationDto responseDto = ResponseReservationDto.builder()
                                                                   .id(1L)
                                                                   .productId(productId)
                                                                   .buyerId(2L)
                                                                   .sellerId(3L)
                                                                   .price(BigDecimal.valueOf(10000))
                                                                   .locationCode("1234567890")
                                                                   .confirmedAt(LocalDateTime.now().plusHours(1))
                                                                   .build();

        Mockito.when(reservationService.createReservation(any(RequestCreateReservationDto.class)))
               .thenReturn(responseDto);

        // when
        mockMvc.perform(post("/reservations")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(requestDto))
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.productId").value(productId))
               .andExpect(jsonPath("$.buyerId").value(2L))
               .andExpect(jsonPath("$.price").value(10000))
               .andExpect(jsonPath("$.locationCode").value("1234567890"));

        // then
        then(reservationService).should().createReservation(any(RequestCreateReservationDto.class));
    }

    @Test
    @DisplayName("해당 상품 전체 예약 조회")
    void testGetAllReservations_Success() throws Exception {
        // given: 상품에 대한 예약 목록
        Long productId = 1L;
        Page<ResponseReservationDto> page = new PageImpl<>(Arrays.asList(
                ResponseReservationDto.builder()
                                      .id(1L)
                                      .productId(productId)
                                      .buyerId(2L)
                                      .price(BigDecimal.valueOf(10000))
                                      .build()
        ));

        // when
        when(reservationService.getAllReservations(eq(productId), anyInt(), anyInt()))
                .thenReturn(page);

        // then
        mockMvc.perform(get("/reservations/products/{productId}", productId)
                       .param("page", "0")
                       .param("size", "20"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].id").value(1L))
               .andExpect(jsonPath("$.content[0].productId").value(productId))
               .andExpect(jsonPath("$.content[0].price").value(10000))
               .andReturn();
    }
}
