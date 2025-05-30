package com.roc.app.competition.assignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, CompetitionParticipant.CompetitionParticipantId> {
    boolean existsCompetitionParticipantByCompetitionIdAndParticipantId(Integer competitionId, Integer participantId);

    default boolean isEnrolled(Integer competitionId, Integer participantId) {
        return existsCompetitionParticipantByCompetitionIdAndParticipantId(competitionId, participantId);
    }
    Integer countByCompetitionId(Integer competitionId);

    List<CompetitionParticipant> findByCompetitionId(Integer competitionId);
}
