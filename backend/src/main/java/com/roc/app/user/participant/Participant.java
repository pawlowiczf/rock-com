package com.roc.app.user.participant;

import com.roc.app.user.general.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "participants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @Column(name = FieldNames.ID, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = FieldNames.ID, nullable = false)
    private User userDetails;

    @Column(name = FieldNames.BIRTH_DATE, nullable = false)
    private LocalDate birthDate;

    public static final class FieldNames {
        public static final String ID = "user_id";
        public static final String BIRTH_DATE = "birth_date";
    }

}