package com.roc.app.competition;


import com.roc.app.competition.dto.CompetitionResponseDto;
import com.roc.app.competition.dto.CompetitionCreateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CompetitionMapper {

    CompetitionResponseDto mapToDto(Competition competition) {
        return new CompetitionResponseDto(
                competition.getCompetitionId(),
                competition.getName(),
                competition.getType(),
                competition.getMatchDurationMinutes(),
                competition.getAvailableCourts(),
                competition.getParticipantsLimit(),
                competition.getStreetAddress(),
                competition.getCity(),
                competition.getPostalCode(),
                competition.getRegistrationOpen()
        );
    }


    Competition mapToEntity(CompetitionCreateRequestDto dto) {
        return Competition.builder()
                .name(dto.name())
                .type(dto.type())
                .matchDurationMinutes(dto.matchDurationMinutes())
                .availableCourts(dto.availableCourts())
                .participantsLimit(dto.participantsLimit())
                .streetAddress(dto.streetAddress())
                .city(dto.city())
                .postalCode(dto.postalCode())
                .registrationOpen(dto.registrationOpen())
                .build();
    }

}
