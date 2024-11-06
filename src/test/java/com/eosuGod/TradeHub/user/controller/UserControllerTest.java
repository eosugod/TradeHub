package com.eosugod.tradehub.user.controller;

import com.eosugod.tradehub.user.dto.request.RequestCreateUserDto;
import com.eosugod.tradehub.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @DisplayName("회원 등록 기능이 올바르게 동작한다.")
    @Test
    void createUser() throws Exception {
        //given
        final RequestCreateUserDto dto = new RequestCreateUserDto("어수현", "죠즈", "000-0000-000-00", "1111100000");

        //when
        ResultActions resultActions = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                                                                   .content(objectMapper.writeValueAsString(dto))
                                                                   .accept(MediaType.APPLICATION_JSON))
                                             .andExpect(status().isOk());
        //then
        then(userService).should().createUser(eq(dto));
    }

}