package com.roc.app.user.referee.licence.dto;

import com.roc.app.competition.CompetitionType;
import com.roc.app.user.referee.licence.RefereeLicence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RefereeCreateLicenceResponseDto(
        @NotNull
        CompetitionType licenceType,
        @NotNull
        Integer refereeId,
        @NotBlank
        String license
) {
    public static RefereeCreateLicenceResponseDto fromModel(RefereeLicence refereeLicence) {
        return new RefereeCreateLicenceResponseDto(
                refereeLicence.getLicenceType(),
                refereeLicence.getReferee().getUserId(),
                refereeLicence.getLicense()
            );
    }
}
