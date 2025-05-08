package com.roc.app.user.general;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.roc.app.user.general.exception.UserNotFoundException;
import com.roc.app.user.participant.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final String EMAIL = "john.doe@example.com";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = Participant.builder()
                .userId(USER_ID)
                .firstName("John")
                .lastName("Doe")
                .email(EMAIL)
                .password("1234567890")
                .city("New York")
                .build();
    }

    @Test
    void getUserByUserId_getsExistingUser(){
        // given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(savedUser));

        // when
        User userResult = userService.getUserByUserId(USER_ID);

        // then
        assertThat(userResult).isEqualTo(savedUser);
    }

    @Test
    void getUserByUserId_throwsErrorForNotExistingUser(){
        // given
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUserId(USER_ID));
    }

    @Test
    void loadUserByUsername_getsExistingUser(){
        // given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(savedUser));

        // when
        UserDetails userResult = userService.loadUserByUsername(EMAIL);

        // then
        assertThat(userResult).isEqualTo(savedUser);
    }

    @Test
    void loadUserByUsername_throwsErrorForNotExistingUser(){
        // given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(EMAIL));
    }
}