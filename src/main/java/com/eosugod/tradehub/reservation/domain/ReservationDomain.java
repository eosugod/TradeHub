package com.eosugod.tradehub.reservation.domain;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.reservation.dto.request.RequestUpdateReservationDto;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import com.eosugod.tradehub.vo.Address;
import com.eosugod.tradehub.vo.Money;
import com.eosugod.tradehub.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationDomain {
    private final Long id;
    private final UserDomain buyer;
    private final ProductDomain product;
    private final Money price;
    private final Address locationCode;
    private final LocalDateTime confirmedAt;
    private final Reservation.ReservationState state;
    private final boolean buyerCompleteRequest;
    private final boolean sellerCompleteRequest;
    private final Long version;
    public ReservationDomain update(RequestUpdateReservationDto dto) {
        return ReservationDomain.builder()
                                .id(this.id)
                                .buyer(this.buyer)
                                .product(this.product)
                                .price(this.price)
                                .locationCode(new Address(dto.getLocationCode()))
                                .confirmedAt(dto.getConfirmedAt())
                                .state(this.state)
                                .buyerCompleteRequest(this.buyerCompleteRequest)
                                .sellerCompleteRequest(this.sellerCompleteRequest)
                                .version(this.version)
                                .build();
    }

    public ReservationDomain updateState(Reservation.ReservationState newState) {
        return ReservationDomain.builder()
                .id(this.id)
                .buyer(this.buyer)
                .product(this.product)
                .price(this.price)
                .locationCode(this.locationCode)
                .confirmedAt(this.confirmedAt)
                .state(newState)
                .buyerCompleteRequest(this.buyerCompleteRequest)
                .sellerCompleteRequest(this.sellerCompleteRequest)
                .version(this.version)
                .build();
    }

    public ReservationDomain cancel() {
        if(this.state != Reservation.ReservationState.CONFIRMED) {
            throw new ExpectedException(ExceptionCode.RESERVATION_NOT_CONFIRMED);
        }
        return ReservationDomain.builder()
                .id(this.id)
                .buyer(this.buyer)
                .product(this.product)
                .price(this.price)
                .locationCode(this.locationCode)
                .confirmedAt(this.confirmedAt)
                .state(Reservation.ReservationState.CANCELLED)
                .buyerCompleteRequest(this.buyerCompleteRequest)
                .sellerCompleteRequest(this.sellerCompleteRequest)
                .version(this.version)
                .build();
    }

    public ReservationDomain completeRequest(boolean isBuyer, boolean isSeller) {
        return ReservationDomain.builder()
                .id(this.id)
                .buyer(this.buyer)
                .product(this.product)
                .price(this.price)
                .locationCode(this.locationCode)
                .confirmedAt(this.confirmedAt)
                .state(this.state)
                .buyerCompleteRequest(isBuyer || this.buyerCompleteRequest)
                .sellerCompleteRequest(isSeller || this.sellerCompleteRequest)
                .version(this.version)
                .build();
    }
}
