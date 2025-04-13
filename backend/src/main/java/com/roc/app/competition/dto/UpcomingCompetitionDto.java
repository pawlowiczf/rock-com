package com.roc.app.competition.dto;

import java.time.LocalDateTime;

public class UpcomingCompetitionDto {
    private Integer competitionId;
    private String typeLabel;
    private String city;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean registrationOpen;

    public UpcomingCompetitionDto(Integer competitionId, String typeLabel, String city,
                                  LocalDateTime startTime, LocalDateTime endTime, Boolean registrationOpen) {
        this.competitionId = competitionId;
        this.typeLabel = typeLabel;
        this.city = city;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registrationOpen = registrationOpen;
    }

    public Integer getCompetitionId() {
        return competitionId;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public String getCity() {
        return city;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Boolean getRegistrationOpen() {
        return registrationOpen;
    }

}
