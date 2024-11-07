package com.eosugod.tradehub.user.controller;

import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.dto.response.ResponseUserDto;
import com.eosugod.tradehub.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody @Valid RequestCreateUserDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseUserDto> readUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.readUser(id));
    }
}
