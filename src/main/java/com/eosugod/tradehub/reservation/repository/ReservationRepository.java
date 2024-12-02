package com.eosugod.tradehub.reservation.repository;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.reservation.entity.Reservation;

import com.eosugod.tradehub.user.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByProductId(Long productId, Pageable pageable);
    boolean existsByProductIdAndBuyerId(Long productId, Long buyerId);
}
