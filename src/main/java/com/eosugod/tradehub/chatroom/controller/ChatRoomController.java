package com.eosugod.tradehub.chatroom.controller;

import com.eosugod.tradehub.chatroom.dto.ChatRoomRequestDto;
import com.eosugod.tradehub.chatroom.entity.ChatRoom;
import com.eosugod.tradehub.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 생성 및 반환
    @PostMapping
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoomRequestDto request) {
        // 채팅방 생성
        ChatRoom chatRoom = chatRoomService.createChatRoom(request.getSenderId(), request.getReceiverId());

        // 생성된 채팅방 반환
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllRooms() {
        return ResponseEntity.ok(chatRoomService.getAllRooms());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoom> getRoom(@PathVariable String roomId) {
        ChatRoom room = chatRoomService.getRoom(roomId);
        if(room != null) return ResponseEntity.ok(room);
        return ResponseEntity.notFound().build();
    }
}
