package com.roc.app.user.participant;

import com.roc.app.user.participant.dto.ParticipantCreateRequestDto;
import com.roc.app.user.participant.dto.ParticipantResponseDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static com.roc.app.user.UserTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {
    private static final LocalDate BIRTH_DATE = LocalDate.ofEpochDay(735);

    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ParticipantService participantService;

    private ParticipantCreateRequestDto validRequestDto;
    private Participant savedParticipant;
    private ParticipantResponseDto expectedParticipantResponseDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new ParticipantCreateRequestDto(
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                PASSWORD,
                CITY,
                PHONE_NUMBER,
                BIRTH_DATE
        );
        savedParticipant = Participant.builder()
                .userId(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .city(CITY)
                .phoneNumber(PHONE_NUMBER)
                .birthDate(BIRTH_DATE)
                .build();

        expectedParticipantResponseDto = new ParticipantResponseDto(
                USER_ID,
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                CITY,
                PHONE_NUMBER,
                BIRTH_DATE
        );
    }


    @Test
    void createParticipantWorksAsExpected() {
        // given
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(participantRepository.save(any())).thenReturn(savedParticipant);

        // when
        ParticipantResponseDto participant = participantService.createParticipant(validRequestDto);

        // then
        assertThat(participant).isEqualTo(expectedParticipantResponseDto);
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