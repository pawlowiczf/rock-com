package com.roc.app.user.match;

import jakarta.persistence.*;

@Entity
@Table(name = "match_statuses")
public class MatchStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusId;

    @Column(name = "status_label")
    private String statusLabel;
}
