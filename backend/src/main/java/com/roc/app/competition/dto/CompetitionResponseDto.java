package com.roc.app.competition.dto;

import com.roc.app.competition.Competition;
import com.roc.app.competition.CompetitionType;

public record CompetitionResponseDto(
        Integer competitionId,
        String name,
        CompetitionType type,
        Integer matchDurationMinutes,
        Integer availableCourts,
        Integer participantsLimit,
        String streetAddress,
        String city,
        String postalCode,
        Boolean registrationOpen
) {
        public Competition toModel() {
                return Competition.builder()
                        .competitionId(competitionId)
                        .name(name)
                        .type(type)
                        .matchDurationMinutes(matchDurationMinutes)
                        .availableCourts(availableCourts)
                        .participantsLimit(participantsLimit)
                        .streetAddress(streetAddress)
                        .city(city)
                        .postalCode(postalCode)
                        .registrationOpen(registrationOpen)
                        .build();
        }

        public static CompetitionResponseDto fromModel(Competition competition) {
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
}
