package com.eosugod.tradehub.user.vo;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Address {
    String sido;
    String gugun;
    String dong;

    public Address(String locationCode) {
        sido = locationCode.substring(0, 3);
        gugun = locationCode.substring(3, 5);
        dong = locationCode.substring(5, 8);
    }

    public String getValue() {
        return sido + gugun + dong;
    }
}
