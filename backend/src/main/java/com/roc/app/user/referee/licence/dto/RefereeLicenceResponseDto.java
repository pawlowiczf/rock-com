package com.roc.app.user.referee.licence.dto;

import com.roc.app.competition.CompetitionType;
import com.roc.app.user.referee.licence.RefereeLicence;

public record RefereeLicenceResponseDto(
        Long refereeLicenceId,
        CompetitionType licenceType,
        Long refereeId,
        String license
) {
    public static RefereeLicenceResponseDto fromModel(RefereeLicence refereeLicence) {
        return new RefereeLicenceResponseDto(
                refereeLicence.getRefereeLicenceId(),
                refereeLicence.getLicenceType(),
                refereeLicence.getReferee().getUserId(),
                refereeLicence.getLicense()
        );
    }
}
