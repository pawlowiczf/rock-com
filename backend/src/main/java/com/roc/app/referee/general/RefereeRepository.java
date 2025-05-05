package com.roc.app.referee.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, Integer> {

}
