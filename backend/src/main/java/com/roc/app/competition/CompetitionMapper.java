package com.roc.app.competition;


import com.roc.app.competition.dto.CompetitionDTO;
import org.springframework.stereotype.Component;

@Component
public class CompetitionMapper {

    CompetitionDTO mapToDto(Competition competition) {
        return CompetitionDTO.builder()
                .competitionId(competition.getCompetitionId())
                .type(competition.getType())
                .matchDurationMinutes(competition.getMatchDurationMinutes())
                .availableCourts(competition.getAvailableCourts())
                .participantsLimit(competition.getParticipantsLimit())
                .streetAddress(competition.getStreetAddress())
                .city(competition.getCity())
                .postalCode(competition.getPostalCode())
                .registrationOpen(competition.getRegistrationOpen())
                .build();
    }

    Competition mapToEntity(CompetitionDTO dto) {
        return Competition.builder()
                .competitionId(dto.getCompetitionId())
                .type(dto.getType())
                .matchDurationMinutes(dto.getMatchDurationMinutes())
                .availableCourts(dto.getAvailableCourts())
                .participantsLimit(dto.getParticipantsLimit())
                .streetAddress(dto.getStreetAddress())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .registrationOpen(dto.getRegistrationOpen())
                .build();
    }
}
