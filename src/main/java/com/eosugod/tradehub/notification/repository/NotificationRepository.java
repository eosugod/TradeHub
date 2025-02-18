package com.eosugod.tradehub.notification.repository;

import com.eosugod.tradehub.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long userId);
}
