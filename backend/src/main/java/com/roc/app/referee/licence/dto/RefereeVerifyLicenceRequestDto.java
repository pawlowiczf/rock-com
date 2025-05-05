package com.roc.app.referee.licence.dto;

import com.roc.app.referee.exception.RefereeNotFoundException;
import com.roc.app.referee.general.Referee;
import com.roc.app.referee.general.RefereeRepository;
import com.roc.app.referee.licence.RefereeLicence;
import com.roc.app.referee.licence.RefereeLicenceId;
import jakarta.validation.constraints.NotBlank;

public record RefereeVerifyLicenceRequestDto(
        @NotBlank
        Integer typeId,
        @NotBlank
        Integer userId,
        @NotBlank
        String license
) {
    public RefereeLicence toModel(RefereeRepository refereeRepository) {
        Referee referee = refereeRepository.findById(userId).orElseThrow(() -> new RefereeNotFoundException("Referee not found. Id: " + userId));

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
