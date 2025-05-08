package com.roc.app.user.referee.licence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

// This class is no longer needed, but I'm keeping it for now in case it's still useful or needed later.
@Deprecated

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefereeLicenceId implements Serializable {

    @Column(name = FieldNames.TYPE_ID)
    private int typeId;

    @Column(name = FieldNames.LICENSE)
    private String licence;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefereeLicenceId that = (RefereeLicenceId) o;
        return typeId == that.typeId && licence.equals(that.licence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, licence);
    }

    public static final class FieldNames {
        public static final String TYPE_ID = "type_id";
        public static final String REFEREE_ID = "referee_id";
        public static final String LICENSE = "license";
    }
}
