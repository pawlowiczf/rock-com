package com.roc.app.user.referee.licence.dto;

import com.roc.app.user.referee.licence.RefereeLicence;
import jakarta.validation.constraints.NotBlank;

public record RefereeAddLicenceResponseDto(
        @NotBlank
        Integer typeId,
        @NotBlank
        Long refereeId,
        @NotBlank
        String license
) {
    public static RefereeAddLicenceResponseDto fromModel(RefereeLicence refereeLicence) {
        return new RefereeAddLicenceResponseDto(
                refereeLicence.getId().getTypeId(),
                refereeLicence.getReferee().getUserId(),
                refereeLicence.getId().getLicence()
        );
    }
}
