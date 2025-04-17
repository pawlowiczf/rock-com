package com.roc.app.competition;

import jakarta.persistence.*;

@Entity
@Table(name = "competition_types")
public class CompetitionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_label")
    private String typeLabel;
}
