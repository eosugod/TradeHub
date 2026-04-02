package com.eosugod.tradehub.chatroom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRoomRequestDto {
    @NotBlank
    private String senderId;
    @NotBlank
    private String receiverId;
}
