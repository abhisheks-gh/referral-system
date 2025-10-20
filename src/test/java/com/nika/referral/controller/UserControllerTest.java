package com.nika.referral.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nika.referral.model.User;
import com.nika.referral.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Tests UserController endpoints for fetching users and their referrals.
 *
 * @author abhisheks-gh
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("abhishek@nika.com");

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("newuser1@nika.com");
    }

    @Test
    void testGetAllUsers_ShouldReturnListOfUsers() throws Exception {
        List<User> users = Arrays.asList(user1, user2);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(org.springframework.test.web.servlet.request.
                MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("abhishek@nika.com"))
                .andExpect(jsonPath("$[1].email").value("newuser1@nika.com"));
    }

    @Test
    void testGetUserById_ShouldReturnUser() throws Exception {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abhishek@nika.com"));
    }

    @Test
    void getReferralsByUserId_ShouldReturnReferrals() throws Exception {
        user1.setDirectReferrals(List.of(user2));
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/api/users/1/referrals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].email").value("newuser1@nika.com"));
    }
}
