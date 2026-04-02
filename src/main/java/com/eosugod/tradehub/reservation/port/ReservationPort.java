package com.eosugod.tradehub.reservation.port;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.reservation.domain.ReservationDomain;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.user.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReservationPort {
    ReservationDomain save(ReservationDomain reservationDomain, Product product, Users user);
    boolean existsByProductIdAndBuyerId(Long productId, Long buyerId);
    Optional<ReservationDomain> findById(Long id);
    Page<ReservationDomain> findByProductId(Long productId, Pageable pageable);
    Page<ReservationDomain> findAllByBuyerIdAndState(Long buyerId, Reservation.ReservationState state, Pageable pageable);
    Page<ReservationDomain> findAllByProduct_SellerIdAndState(Long sellerId, Reservation.ReservationState state, Pageable pageable);
}
