package com.roc.app.competitionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "competition_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_label", nullable = false, length = 20)
    private String typeLabel;
}
