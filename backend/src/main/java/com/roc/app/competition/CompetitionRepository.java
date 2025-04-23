package com.roc.app.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findByCompetitionTypeTypeId(Integer typeId);

    @Query("SELECT c FROM Competition c WHERE c.registrationOpen = true")
    List<Competition> findAllOpenCompetitions();

    @Query("SELECT c FROM Competition c WHERE c.city = :city")
    List<Competition> findCompetitionsByCity(String city);
}
