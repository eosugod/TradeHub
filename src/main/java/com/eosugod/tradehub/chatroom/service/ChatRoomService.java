package com.eosugod.tradehub.chatroom.service;

import com.eosugod.tradehub.chatroom.entity.ChatRoom;
import com.eosugod.tradehub.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 생성 (없으면 새로 생성)
    public ChatRoom createChatRoom(String senderId, String receiverId) {
        String roomId = generateRoomId(senderId, receiverId);

        // 이미 존재하는 채팅방이 있다면 해당 채팅방을 리턴
        ChatRoom existingChatRoom = chatRoomRepository.findByRoomId(roomId)
                                                      .orElseGet(() -> {
                                                          // 새로운 채팅방을 생성하고 저장
                                                          ChatRoom newChatRoom = new ChatRoom();
                                                          newChatRoom.setRoomId(roomId);
                                                          newChatRoom.setName(senderId + " and " + receiverId + "'s chat");
                                                          return chatRoomRepository.save(newChatRoom);
                                                      });

        return existingChatRoom;
    }

    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getRoom(String roomId) {
        return chatRoomRepository.findById(roomId).orElse(null);
    }

    private String generateRoomId(String senderId, String receiverId) {
        // ID 순서에 관계없이 항상 동일한 roomId 생성
        return senderId.compareTo(receiverId) < 0 ? senderId + "_" + receiverId : receiverId + "_" + senderId;
    }
}
