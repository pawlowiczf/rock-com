package com.roc.app.competition;


import com.roc.app.competition.dto.CompetitionDTO;
import com.roc.app.competition.dto.CreateCompetitionDto;
import org.springframework.stereotype.Component;

@Component
public class CompetitionMapper {

    CompetitionDTO mapToDto(Competition competition) {
        return new CompetitionDTO(
                competition.getCompetitionId(),
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


    Competition mapToEntity(CreateCompetitionDto dto) {
        return Competition.builder()
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
