package com.eosugod.tradehub.reservation.entity;

import com.eosugod.tradehub.product.entity.Product;
import com.eosugod.tradehub.user.entity.Users;
import com.eosugod.tradehub.product.vo.Address;
import com.eosugod.tradehub.product.vo.Money;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId;
    private Long buyerId;
    @Embedded
    private Money price;
    @Embedded
    private Address locationCode;
    private LocalDateTime confirmedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public void setProduct(Product product) {
        this.product = product;
        this.sellerId = product.getSellerId();
    }

    public void setUser(Users user) {
        this.user = user;
        this.buyerId = user.getId();
    }
}
