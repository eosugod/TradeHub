package com.eosugod.tradehub.reservation.repository;

import com.eosugod.tradehub.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByProductId(Long productId, Pageable pageable);
    boolean existsByProductIdAndBuyerId(Long productId, Long buyerId);
    Page<Reservation> findAllByBuyerIdAndState(Long buyerId, Reservation.ReservationState state, Pageable pageable);
    Page<Reservation> findAllBySellerIdAndState(Long sellerId, Reservation.ReservationState state, Pageable pageable);
}
