package com.roc.app.bracket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BracketRepository extends JpaRepository<Bracket, Integer> {
    @Query("SELECT b.nextMatchId FROM Bracket b WHERE b.matchId = :matchId")
    Optional<Integer> findNextMatchIdByMatchId(@Param("matchId") Integer matchId);
}
