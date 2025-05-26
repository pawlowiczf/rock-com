package com.roc.app.competition;

import com.roc.app.competition.dto.UpcomingCompetitionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionDateRepository extends JpaRepository<CompetitionDate, Integer> {

    @Query("SELECT new com.roc.app.competition.dto.UpcomingCompetitionDto(" +
            "c.competitionId, c.name, c.type, c.city, MIN(cd.startTime), MAX(cd.endTime), c.registrationOpen) " +
            "FROM CompetitionDate cd " +
            "JOIN cd.competition c " +
            "WHERE cd.startTime > CURRENT_TIMESTAMP " +
            "GROUP BY c.competitionId, c.type, c.city, c.registrationOpen " +
            "ORDER BY MIN(cd.startTime) ASC ")
    List<UpcomingCompetitionDto> findUpcomingCompetitions();

    @Query("SELECT cd FROM CompetitionDate cd WHERE cd.competition.competitionId = :competitionId ORDER BY cd.startTime")
    List<CompetitionDate> findAllByCompetitionId(@Param("competitionId") Integer competitionId);
}
