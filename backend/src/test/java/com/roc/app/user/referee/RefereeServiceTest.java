package com.roc.app.user.referee;

import com.roc.app.user.referee.general.Referee;
import com.roc.app.user.referee.general.RefereeRepository;
import com.roc.app.user.referee.general.RefereeService;
import com.roc.app.user.referee.general.dto.RefereeCreateRequestDto;
import com.roc.app.user.referee.general.dto.RefereeResponseDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.roc.app.user.UserTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefereeServiceTest {
    @Mock
    private RefereeRepository refereeRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private RefereeService refereeService;

    private RefereeCreateRequestDto validRequestDto;
    private Referee savedReferee;
    private RefereeResponseDto expectedRefereeResponseDto;

    @BeforeEach
    void setUp() {
        validRequestDto = new RefereeCreateRequestDto(
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                PASSWORD,
                CITY,
                PHONE_NUMBER
        );
        savedReferee = Referee.builder()
                .userId(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .city(CITY)
                .phoneNumber(PHONE_NUMBER)
                .build();
        expectedRefereeResponseDto = new RefereeResponseDto(
                USER_ID,
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                CITY,
                PHONE_NUMBER
        );
    }

    @Test
    void createRefereeWorksAsExpected() {
        // given
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(refereeRepository.save(any())).thenReturn(savedReferee);

        // when
        RefereeResponseDto referee = refereeService.createReferee(validRequestDto);

        // then
        assertThat(referee).isEqualTo(expectedRefereeResponseDto);
    }

    @Test
    void createRefereeUser_VerifiesTransactionalAnnotation() {
        try {
            assertTrue(RefereeService.class
                    .getMethod("createReferee", RefereeCreateRequestDto.class)
                    .isAnnotationPresent(Transactional.class));
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        }
    }
}
