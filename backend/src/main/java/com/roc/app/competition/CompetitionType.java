package com.roc.app.competition;

import jakarta.persistence.*;

@Entity
@Table(name = CompetitionType.FieldNames.TABLE_NAME)
public class CompetitionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.TYPE_ID)
    private Integer typeId;

    @Column(name = FieldNames.TYPE_LABEL)
    private String typeLabel;

    public CompetitionType() {}

    public Integer getTypeId() {
        return typeId;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public static final class FieldNames {
        public static final String TABLE_NAME = "competition_types";
        public static final String TYPE_ID = "type_id";
        public static final String TYPE_LABEL = "type_label";
    }
}
