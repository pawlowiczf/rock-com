package com.roc.app.competition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findByType(CompetitionType type);

    List<Competition> findByRegistrationOpen(boolean registrationOpen);

    List<Competition> findByCity(String city);

}
