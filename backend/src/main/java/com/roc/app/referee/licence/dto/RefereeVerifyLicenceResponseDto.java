package com.roc.app.referee.licence.dto;

import com.roc.app.referee.general.RefereeRepository;
import com.roc.app.referee.licence.RefereeLicence;
import jakarta.validation.constraints.NotBlank;

public record RefereeVerifyLicenceResponseDto(
        @NotBlank
        Integer typeId,
        @NotBlank
        Integer refereeId,
        @NotBlank
        String license
) {
    public static RefereeVerifyLicenceResponseDto fromModel(RefereeLicence refereeLicence) {
        return new RefereeVerifyLicenceResponseDto(
                refereeLicence.getTypeId(),
                refereeLicence.getReferee().getUser().getUserId().intValue(),
                refereeLicence.getLicense()
        );
    }
}
