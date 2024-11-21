package com.eosugod.tradehub.reservation.mapper;

import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationMapper {
    public static Reservation toEntity(RequestCreateReservationDto requestDto, Product product, Users buyer) {
        Reservation reservation = Reservation.builder()
                          .product(product)
                          .user(buyer)
                          .price(new Money(requestDto.getPrice()))
                          .locationCode(new Address(requestDto.getLocationCode()))
                          .confirmedAt(requestDto.getConfirmedAt())
                          .build();
        reservation.setProduct(product);
        reservation.setUser(buyer);
        return reservation;
    }

    public static ResponseReservationDto toResponseDto(Reservation reservation) {
        return ResponseReservationDto.builder()
                .id(reservation.getId())
                .productId(reservation.getProduct().getId())
                .sellerId(reservation.getProduct().getSellerId())
                .buyerId(reservation.getUser().getId())
                .price(reservation.getPrice().getValue())
                .locationCode(reservation.getLocationCode().getValue())
                .confirmedAt(reservation.getConfirmedAt())
                .build();
    }
}

