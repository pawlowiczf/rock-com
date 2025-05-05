package com.roc.app.referee.licence;

import com.roc.app.referee.general.Referee;
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

    @EmbeddedId
    private RefereeLicenceId id;

    @Column(name = FieldNames.TYPE_ID, nullable = false)
    private int typeId;

    @ManyToOne
    @JoinColumn(name = FieldNames.REFEREE_ID, nullable = false)
    private Referee referee;

    @Column(name = FieldNames.LICENSE, nullable = false)
    private String license;

    public static final class FieldNames {
        public static final String TYPE_ID = "type_id";
        public static final String REFEREE_ID = "referee_id";
        public static final String LICENSE = "license";
    }
}
