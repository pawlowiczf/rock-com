package com.roc.app.user.referee.licence;

import com.roc.app.competition.CompetitionType;
import com.roc.app.user.referee.general.Referee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "referee_licences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefereeLicence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.REFEREE_LICENCE_ID)
    private Long refereeLicenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = FieldNames.LICENCE_TYPE)
    private CompetitionType licenceType;

    @Column(name = FieldNames.LICENSE)
    private String license;

    @ManyToOne
    @JoinColumn(name = FieldNames.REFEREE_ID, referencedColumnName = "user_id", nullable = false)
    private Referee referee;

    public static final class FieldNames {
        public static final String REFEREE_LICENCE_ID = "referee_licence_id";
        public static final String REFEREE_ID = "referee_id";
        public static final String LICENSE = "license";
        public static final String LICENCE_TYPE = "licence_type";
    }
}
