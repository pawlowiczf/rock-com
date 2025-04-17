package com.roc.app.competition;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "competition_dates")
public class CompetitionDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Integer dateId;

    @ManyToOne
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
