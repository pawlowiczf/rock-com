package com.roc.app.user.referee.licence;

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

    @EmbeddedId
    private RefereeLicenceId id;

    @ManyToOne
    @JoinColumn(name = FieldNames.REFEREE_ID, nullable = false)
    private Referee referee;

    public static final class FieldNames {
        public static final String TYPE_ID = "type_id";
        public static final String REFEREE_ID = "referee_id";
        public static final String LICENSE = "license";
    }
}
