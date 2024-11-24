package com.eosugod.tradehub.product.port;

import com.eosugod.tradehub.product.domain.ProductDomain;
import com.eosugod.tradehub.product.dto.request.RequestCreateProductDto;

public interface ProductPort {
    ProductDomain save(RequestCreateProductDto dto);
}
