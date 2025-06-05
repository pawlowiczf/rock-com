package com.roc.app.competition.referee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionRefereeRepository extends JpaRepository<CompetitionReferee, CompetitionReferee.CompetitionRefereeId> {
    List<CompetitionReferee> findByCompetitionId(Integer competitionId);
}
