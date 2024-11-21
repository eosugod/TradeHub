package com.eosugod.tradehub.reservation.repository;

import com.eosugod.tradehub.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);
    boolean existsByProductId(Long productId);
}
