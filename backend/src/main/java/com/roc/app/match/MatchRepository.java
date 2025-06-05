package com.roc.app.match;

import com.roc.app.match.dto.ParticipantMatchResponseDto;
import com.roc.app.match.dto.RefereeMatchResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {

    @Query("""
SELECT new com.roc.app.match.dto.ParticipantMatchResponseDto(
    m.matchId, m.competition.competitionId, m.matchDate,
    m.status,
    CONCAT(u.firstName, ' ', u.lastName),
    m.score,
    CASE WHEN m.winnerId = cp.participantId THEN true ELSE false END,
    CONCAT(ref.firstName, ' ', ref.lastName)
)
FROM Match m
JOIN CompetitionParticipant cp ON (
    (m.player1Id = cp.participantId OR m.player2Id = cp.participantId)
    AND m.competition.competitionId = cp.competitionId
)
JOIN User u ON (
    (m.player1Id = cp.participantId AND m.player2Id = u.userId) OR
    (m.player2Id = cp.participantId AND m.player1Id = u.userId)
)
LEFT JOIN User ref ON m.refereeId = ref.userId
WHERE cp.participantId = :userId
ORDER BY m.matchDate DESC
""")
    List<ParticipantMatchResponseDto> findParticipantMatches(@Param("userId") Integer userId);
    
    @Query("""
    SELECT new com.roc.app.match.dto.RefereeMatchResponseDto(
        m.matchId, m.competition.competitionId, m.matchDate, m.status,
        CONCAT(u1.firstName, ' ', u1.lastName),
        CONCAT(u2.firstName, ' ', u2.lastName),
        m.score
    )
    FROM Match m
    JOIN User u1 ON m.player1Id = u1.userId
    JOIN User u2 ON m.player2Id = u2.userId
    WHERE m.refereeId = :refereeId
    ORDER BY m.matchDate DESC
""")
    List<RefereeMatchResponseDto> findRefereeMatches(@Param("refereeId") Integer refereeId);

    List<Match> findByCompetition_CompetitionIdAndStatus(Integer competitionId, MatchStatus status);

    List<Match> findByCompetitionCompetitionId(Integer competitionId);

    List<Match> findByRefereeIdAndCompetitionCompetitionId(Integer refereeId, Integer competitionId);

}
