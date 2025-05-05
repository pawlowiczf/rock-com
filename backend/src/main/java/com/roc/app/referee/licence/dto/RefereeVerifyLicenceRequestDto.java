package com.roc.app.referee.licence.dto;

import com.roc.app.referee.exception.RefereeNotFoundException;
import com.roc.app.referee.general.Referee;
import com.roc.app.referee.general.RefereeRepository;
import com.roc.app.referee.general.RefereeService;
import com.roc.app.referee.licence.RefereeLicence;
import com.roc.app.referee.licence.RefereeLicenceId;
import com.roc.app.user.general.User;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record RefereeVerifyLicenceRequestDto(
        @NotBlank
        Integer typeId,
        @NotBlank
        Integer refereeId,
        @NotBlank
        String license
) {
    public RefereeLicence toModel(RefereeRepository refereeRepository) {
        User user = User.builder().userId((long) refereeId).build();
        Referee referee = refereeRepository.findById(user).orElseThrow(() -> new RefereeNotFoundException("Referee not found. Id: " + refereeId));

        return RefereeLicence.builder()
                .typeId(typeId)
                .referee(referee)
                .license(license)
                .build();
    }
}
