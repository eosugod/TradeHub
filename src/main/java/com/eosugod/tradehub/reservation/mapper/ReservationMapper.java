package com.eosugod.tradehub.reservation.mapper;

import com.eosugod.tradehub.product.mapper.ProductMapper;
import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import com.eosugod.tradehub.reservation.domain.ReservationDomain;
import com.eosugod.tradehub.reservation.dto.response.ResponseReservationDto;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.reservation.dto.request.RequestCreateReservationDto;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.user.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationMapper {
    public static ReservationDomain persistenceToDomain(Reservation reservation) {
        return ReservationDomain.builder()
                .id(reservation.getId())
                .buyer(UserMapper.persistenceToDomain(reservation.getBuyer()))
                .product(ProductMapper.persistenceToDomain(reservation.getProduct()))
                .price(new Money(reservation.getPrice().getValue()))
                .locationCode(new Address(reservation.getLocationCode().getValue()))
                .confirmedAt(reservation.getConfirmedAt())
                .state(reservation.getState())
                .build();
    }

    public static Reservation dtoToPersistence(RequestCreateReservationDto dto, Product product, Users buyer) {
        return Reservation.builder()
                .product(product)
                .buyer(buyer)
                .price(new Money(dto.getPrice()))
                .locationCode(new Address(dto.getLocationCode()))
                .confirmedAt(dto.getConfirmedAt())
                .state(Reservation.ReservationState.PENDING)
                .build();
    }

    public static Reservation domainToPersistence(ReservationDomain reservationDomain, Product product, Users buyer) {
        return Reservation.builder()
                .id(reservationDomain.getId())
                .product(product)
                .buyer(buyer)
                .price(new Money(reservationDomain.getPrice().getValue()))
                .locationCode(new Address(reservationDomain.getLocationCode().getValue()))
                .confirmedAt(reservationDomain.getConfirmedAt())
                .state(reservationDomain.getState())
                .build();
    }

    public static ResponseReservationDto domainToDto(ReservationDomain reservationDomain) {
        return ResponseReservationDto.builder()
                .id(reservationDomain.getId())
                .sellerId(reservationDomain.getProduct().getSellerId())
                .buyerId(reservationDomain.getBuyer().getId())
                .productId(reservationDomain.getProduct().getId())
                .price(reservationDomain.getPrice().getValue())
                .locationCode(reservationDomain.getLocationCode().getValue())
                .confirmedAt(String.valueOf(reservationDomain.getConfirmedAt()))
                .state(reservationDomain.getState())
                .build();
    }
}

