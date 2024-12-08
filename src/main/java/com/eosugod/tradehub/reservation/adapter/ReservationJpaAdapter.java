package com.eosugod.tradehub.reservation.adapter;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.reservation.domain.ReservationDomain;
import com.eosugod.tradehub.reservation.entity.Reservation;
import com.eosugod.tradehub.reservation.mapper.ReservationMapper;
import com.eosugod.tradehub.reservation.port.ReservationPort;
import com.eosugod.tradehub.reservation.repository.ReservationRepository;
import com.eosugod.tradehub.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationJpaAdapter implements ReservationPort {
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationDomain save(ReservationDomain reservationDomain, Product product, Users buyer) {
        Reservation reservation = ReservationMapper.domainToPersistence(reservationDomain, product, buyer);
        return ReservationMapper.persistenceToDomain(reservationRepository.save(reservation));
    }

    @Override
    public Optional<ReservationDomain> findById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationMapper::persistenceToDomain);
    }

    @Override
    public Page<ReservationDomain> findByProductId(Long productId, Pageable pageable) {
        return reservationRepository.findByProductId(productId, pageable)
                .map(ReservationMapper::persistenceToDomain);
    }

    @Override
    public boolean existsByProductIdAndBuyerId(Long productId, Long buyerId) {
        return reservationRepository.existsByProductIdAndBuyerId(productId, buyerId);
    }

    @Override
    public Page<ReservationDomain> findAllByBuyerIdAndState(Long buyerId, Reservation.ReservationState state, Pageable pageable) {
        return reservationRepository.findAllByBuyerIdAndState(buyerId, state, pageable)
                                    .map(ReservationMapper::persistenceToDomain);
    }

    @Override
    public Page<ReservationDomain> findAllByProduct_SellerIdAndState(Long sellerId, Reservation.ReservationState state, Pageable pageable) {
        return reservationRepository.findAllByProduct_SellerIdAndState(sellerId, state, pageable)
                                    .map(ReservationMapper::persistenceToDomain);
    }
}
