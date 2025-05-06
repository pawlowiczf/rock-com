package com.roc.app.user.referee.licence.dto;

import com.roc.app.user.referee.exception.RefereeNotFoundException;
import com.roc.app.user.referee.general.Referee;
import com.roc.app.user.referee.general.RefereeRepository;
import com.roc.app.user.referee.licence.RefereeLicence;
import com.roc.app.user.referee.licence.RefereeLicenceId;
import jakarta.validation.constraints.NotBlank;

public record RefereeAddLicenceRequestDto(
        @NotBlank
        Integer typeId,
        @NotBlank
        Long userId,
        @NotBlank
        String license
) {
    public RefereeLicence toModel(Referee referee) {
        RefereeLicenceId id = RefereeLicenceId
                .builder()
                .typeId(typeId)
                .licence(license)
                .build();

        return RefereeLicence.builder()
                .id(id)
                .referee(referee)
                .build();
    }
}
