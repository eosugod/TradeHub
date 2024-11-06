package com.eosugod.tradehub.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RequestCreateUserDto(@NotBlank String name, @NotBlank String nickName, @NotBlank String account,
                                   @NotBlank @Size(min = 10, max = 10) String locationCode) {
}
