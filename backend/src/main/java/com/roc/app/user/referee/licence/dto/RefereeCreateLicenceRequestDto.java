package com.roc.app.user.referee.licence.dto;

import com.roc.app.competition.CompetitionType;
import com.roc.app.user.referee.general.Referee;
import com.roc.app.user.referee.licence.RefereeLicence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefereeCreateLicenceRequestDto(
        @NotNull(message = "Provide correct licenceType: TENNIS_OUTDOOR | TABLE_TENNIS | BADMINTON")
        CompetitionType licenceType,
        @NotNull(message = "Provide correct refereeID") @Min(1)
        Long userId,
        @NotBlank(message = "Provide referee license")
        String license
) {
    public RefereeLicence toModel(Referee referee) {

        return RefereeLicence.builder()
                .referee(referee)
                .licenceType(licenceType)
                .license(license)
                .build();
    }
}
