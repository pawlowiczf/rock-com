package com.roc.app.competition.assignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, CompetitionParticipant.CompetitionParticipantId> {
    boolean existsCompetitionParticipantByCompetitionIdAndParticipantId(Integer competitionId, Long participantId);

    default boolean isEnrolled(Integer competitionId, Long participantId) {
        return existsCompetitionParticipantByCompetitionIdAndParticipantId(competitionId, participantId);
    }
    Long countByCompetitionId(Integer competitionId);


    List<CompetitionParticipant> findCompetitionParticipantsByCompetitionId(Integer competitionId);
}
