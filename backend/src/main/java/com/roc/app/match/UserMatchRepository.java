package com.roc.app.match;

import com.roc.app.match.dto.UserMatchDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMatchRepository extends Repository<Match, Integer> {

    @Query("""
        SELECT new com.roc.app.match.dto.UserMatchDto(
            m.matchId, m.competition.competitionId, m.matchDate,
            s.statusLabel,
            CONCAT(u.firstName, ' ', u.lastName),
            m.score,
            CASE WHEN m.winnerId = cp.participantId THEN true ELSE false END
        )
        FROM Match m
        JOIN MatchStatus s ON m.status.statusId = s.statusId
        JOIN CompetitionParticipant cp ON (
            (m.player1Id = cp.participantId OR m.player2Id = cp.participantId)
            AND m.competition.competitionId = cp.competitionId
        )
        JOIN User u ON (
            (m.player1Id = cp.participantId AND m.player2Id = u.userId) OR
            (m.player2Id = cp.participantId AND m.player1Id = u.userId)
        )
        WHERE cp.participantId = :userId
        ORDER BY m.matchDate DESC
    """)
    List<UserMatchDto> findUserMatches(@Param("userId") Integer userId);
}
