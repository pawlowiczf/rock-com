package com.roc.app.competitionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionTypeRepository extends JpaRepository<CompetitionType, Integer> {
    boolean existsByTypeLabel(String typeLabel);
}
