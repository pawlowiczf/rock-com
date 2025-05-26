package com.roc.app.competition.assignment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, Integer> {
    Long countByCompetitionId(Integer competitionId);
}
