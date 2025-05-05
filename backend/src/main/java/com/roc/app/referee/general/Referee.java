package com.roc.app.referee.general;

import com.roc.app.referee.licence.RefereeLicence;
import com.roc.app.user.general.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "referees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Referee {

    @Id
    @OneToOne
    @JoinColumn(name = FieldNames.USER_ID)
    private User user;

    @OneToMany(mappedBy = "referee")
    private List<RefereeLicence> refereeLicences;

    public static final class FieldNames {
        public static final String USER_ID = "user_id";
    }
}
