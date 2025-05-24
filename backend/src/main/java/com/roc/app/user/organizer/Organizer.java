package com.roc.app.user.organizer;

import com.roc.app.user.general.User;
import com.roc.app.user.general.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "organizers")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Organizer extends User {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(UserRole.ORGANIZER);
    }
}
