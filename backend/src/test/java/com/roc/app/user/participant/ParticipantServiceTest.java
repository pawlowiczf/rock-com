package com.roc.app.user.participant;

import com.roc.app.user.general.User;
import com.roc.app.user.general.UserService;
import com.roc.app.user.general.dto.UserResponseDto;
import com.roc.app.user.participant.dto.ParticipantCreateRequestDto;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ParticipantService participantService;

    private ParticipantCreateRequestDto validRequestDto;
    private User savedUser;
    private Participant savedParticipant;
    private UserResponseDto expectedUserResponseDto;
    private ParticipantResponseDto expectedParticipantResponseDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new ParticipantCreateRequestDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "New York",
                "1234567890",
                LocalDate.ofEpochDay(735)
        );

        savedUser = validRequestDto.toUserCreateRequestDto().toModel();

        savedUser.setUserId(1L);

        savedParticipant = Participant.builder()
                .userDetails(savedUser)
                .birthDate(validRequestDto.birthDate())
                .build();

        expectedUserResponseDto = UserResponseDto.fromModel(savedUser);
        expectedParticipantResponseDto = ParticipantResponseDto.fromModel(savedParticipant);
    }


    @Test
    void createParticipantWorksAsExpected() {
        // given
        when(userService.createUser(any())).thenReturn(expectedUserResponseDto);
        when(userService.getUserByUserId(any())).thenReturn(savedUser);
        when(participantRepository.save(any())).thenReturn(savedParticipant);

        // when
        ParticipantResponseDto participant = participantService.createParticipant(validRequestDto);

        // then
        assertThat(participant)
                .usingRecursiveAssertion()
                .ignoringFields("userId")
                .isEqualTo(expectedParticipantResponseDto);
    }

    @Test
    void createParticipantUser_VerifiesTransactionalAnnotation() {
        try {
            assertTrue(ParticipantService.class
                    .getMethod("createParticipant", ParticipantCreateRequestDto.class)
                    .isAnnotationPresent(Transactional.class));
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }
}