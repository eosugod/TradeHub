package com.eosugod.tradehub.reservation.port;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.reservation.domain.ReservationDomain;
import com.eosugod.tradehub.user.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReservationPort {
    ReservationDomain save(ReservationDomain reservationDomain, Product product, Users user);
    boolean existsByProductIdAndBuyerId(Long productId, Long buyerId);
    Optional<ReservationDomain> findById(Long id);
    Page<ReservationDomain> findByProductId(Long productId, Pageable pageable);
}
