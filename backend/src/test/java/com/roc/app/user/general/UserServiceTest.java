package com.roc.app.user.general;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.roc.app.user.general.dto.UserCreateRequestDto;
import com.roc.app.user.general.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserCreateRequestDto validRequestDto;
    private User savedUser;
    private UserResponseDto expectedUserResponseDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new UserCreateRequestDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "New York",
                "1234567890"
        );

        savedUser = new User(
                validRequestDto.firstName(),
                validRequestDto.lastName(),
                validRequestDto.email(),
                validRequestDto.city(),
                validRequestDto.phoneNumber()
        );

        savedUser.setUserId(1L);

        expectedUserResponseDto = UserResponseDto.fromModel(savedUser);
    }

    @Test
    void createUserWorksAsExpected() {
        // given
        when(userRepository.save(any(User.class))).thenReturn(savedUser);


        // when
        UserResponseDto user = userService.createUser(validRequestDto);

        // then
        verify(userRepository).save(any(User.class));
        assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("userId")
                .isEqualTo(expectedUserResponseDto);
    }

    @Test
    void createUser_VerifiesTransactionalAnnotation() {
        try {
            assertTrue(UserService.class
                    .getMethod("createUser", UserCreateRequestDto.class)
                    .isAnnotationPresent(Transactional.class));
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }


}