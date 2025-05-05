package com.roc.app.referee.licence;

import com.roc.app.referee.general.RefereeRepository;
import com.roc.app.referee.licence.dto.RefereeVerifyLicenceRequestDto;
import com.roc.app.referee.licence.dto.RefereeVerifyLicenceResponseDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class RefereeLicenceService {
    private final RefereeLicenceRepository refereeLicenceRepository;
    private final RefereeRepository refereeRepository;

    public RefereeLicenceService(RefereeRepository refereeRepository, RefereeLicenceRepository refereeLicenceRepository) {
        this.refereeLicenceRepository = refereeLicenceRepository;
        this.refereeRepository = refereeRepository;
    }

    @Transactional
    public RefereeVerifyLicenceResponseDto verifyRefereeLicence(RefereeVerifyLicenceRequestDto refereeVerifyLicenceRequestDto) {

            // TODO VERIFY REFEREE LICENCE

            RefereeLicence refereeLicence = refereeVerifyLicenceRequestDto.toModel(refereeRepository);
            refereeLicence = refereeLicenceRepository.save(refereeLicence);
            return RefereeVerifyLicenceResponseDto.fromModel(refereeLicence);
    }
}
