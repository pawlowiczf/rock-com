package com.roc.app.competition;

import com.roc.app.competition.dto.UpcomingCompetitionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompetitionDateRepository extends JpaRepository<CompetitionDate, Integer> {

    @Query("SELECT new com.roc.app.competition.dto.UpcomingCompetitionDto(" +
            "cd.competition.competitionId, cd.competition.type.typeLabel, cd.competition.city, " +
            "cd.startTime, cd.endTime, cd.competition.registrationOpen) " +
            "FROM CompetitionDate cd " +
            "WHERE cd.startTime > CURRENT_TIMESTAMP " +
            "ORDER BY cd.startTime ASC")
    List<UpcomingCompetitionDto> findUpcomingCompetitions();
}
