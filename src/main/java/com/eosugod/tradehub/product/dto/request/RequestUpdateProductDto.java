package com.eosugod.tradehub.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateProductDto {
    @Min(value = 1000, message = "최소 가격은 1,000원 입니다.")
    private BigDecimal price;
    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    private String text;
    @NotBlank @Size(min=10, max=10)
    private String locationCode;
    private String thumbNailImage;
}
