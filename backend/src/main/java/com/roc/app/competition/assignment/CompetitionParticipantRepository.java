package com.roc.app.competition.assignment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, CompetitionParticipant.CompetitionParticipantId> {
    boolean existsCompetitionParticipantByCompetitionIdAndParticipantId(Integer competitionId, Long participantId);

    default boolean isEnrolled(Integer competitionId, Long participantId) {
        return existsCompetitionParticipantByCompetitionIdAndParticipantId(competitionId, participantId);
    }
    Long countByCompetitionId(Integer competitionId);
}
