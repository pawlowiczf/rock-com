package com.roc.app.user.referee.general;

import com.roc.app.user.general.UserRole;
import com.roc.app.user.referee.licence.RefereeLicence;
import com.roc.app.user.general.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "referees")
@PrimaryKeyJoinColumn(name = Referee.FieldNames.USER_ID)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Referee extends User {
    @OneToMany(mappedBy = "referee")
    private List<RefereeLicence> refereeLicences;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(UserRole.REFEREE);
    }

    public static final class FieldNames {
        public static final String USER_ID = "user_id";
    }
}
