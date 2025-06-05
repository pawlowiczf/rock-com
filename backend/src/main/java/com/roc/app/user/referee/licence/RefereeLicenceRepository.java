package com.roc.app.user.referee.licence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefereeLicenceRepository extends JpaRepository<RefereeLicence, Integer> {

    List<RefereeLicence> findByRefereeUserId(Integer userId);
    RefereeLicence findByLicense(String license);
}
