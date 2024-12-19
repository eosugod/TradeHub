package com.eosugod.tradehub.user.validator;

import com.eosugod.tradehub.user.port.UserPort;
import com.eosugod.tradehub.util.ExceptionCode;
import com.eosugod.tradehub.util.ExpectedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserPort userPort;

    public void existAccount(String account){
        if(userPort.existsByAccount(account)){
            throw new ExpectedException(ExceptionCode.EXIST_ACCOUNT);
        }
    }
    public void existNickName(String nickName){
        if(userPort.existsByNickName(nickName)){
            throw new ExpectedException(ExceptionCode.EXIST_NICKNAME);
        }
    }

    public void validateChargeAmount(Long amount) {
        if(amount < 1000) {
            throw new ExpectedException(ExceptionCode.INVALID_POINT);
        }
    }
}
