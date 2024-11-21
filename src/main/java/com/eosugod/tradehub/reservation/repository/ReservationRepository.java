package com.eosugod.tradehub.reservation.repository;

import com.eosugod.tradehub.reservation.entity.Reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);
    boolean existsByProductId(Long productId);
    Page<Reservation> findAllByProductId(Long productId, Pageable pageable);
}
