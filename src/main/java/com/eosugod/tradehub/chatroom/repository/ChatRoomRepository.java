package com.eosugod.tradehub.chatroom.repository;

import com.eosugod.tradehub.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByRoomId(String roomId);
}
