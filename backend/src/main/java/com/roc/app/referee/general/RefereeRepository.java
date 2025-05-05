package com.roc.app.referee.general;

import com.roc.app.referee.licence.RefereeLicenceId;
import com.roc.app.user.general.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefereeRepository extends JpaRepository<Referee, User> {
}
