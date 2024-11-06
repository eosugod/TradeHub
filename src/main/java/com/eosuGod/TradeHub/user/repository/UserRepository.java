package com.eosugod.tradehub.user.repository;


import com.eosugod.tradehub.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByAccount(String account);

    boolean existsByNickName(String nickName);
}
