package com.roc.app.user.participant;

import com.roc.app.user.general.User;
import com.roc.app.user.general.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "participants")
@PrimaryKeyJoinColumn(name = Participant.FieldNames.ID)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Participant extends User {
    @Column(name = FieldNames.BIRTH_DATE, nullable = false)
    private LocalDate birthDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(UserRole.PARTICIPANT);
    }

    public static final class FieldNames {
        public static final String ID = "user_id";
        public static final String BIRTH_DATE = "birth_date";
    }

}