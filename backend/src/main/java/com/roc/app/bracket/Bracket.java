package com.roc.app.bracket;

import com.roc.app.match.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = Bracket.FieldNames.TABLE_NAME)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bracket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FieldNames.ID)
    private int bracketId;

    @Column(name = FieldNames.MATCH_ID)
    private int matchId;

    @Column(name = FieldNames.NEXT_MATCH_ID)
    private int nextMatchId;

    public static final class FieldNames {
        public static final String TABLE_NAME = "brackets";
        public static final String ID = "bracket_id";
        public static final String MATCH_ID = "match_id";
        public static final String NEXT_MATCH_ID = "next_match_id";
    }
}
