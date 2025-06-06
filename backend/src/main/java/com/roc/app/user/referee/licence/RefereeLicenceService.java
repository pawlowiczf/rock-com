package com.roc.app.user.referee.licence;

import com.roc.app.user.referee.general.Referee;
import com.roc.app.user.referee.general.RefereeRepository;
import com.roc.app.user.referee.licence.dto.RefereeCreateLicenceRequestDto;
import com.roc.app.user.referee.licence.dto.RefereeCreateLicenceResponseDto;
import com.roc.app.user.referee.licence.dto.RefereeLicenceResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefereeLicenceService {
    private final RefereeLicenceRepository refereeLicenceRepository;
    private final RefereeRepository refereeRepository;

    public RefereeLicenceService(RefereeLicenceRepository refereeLicenceRepository, RefereeRepository refereeRepository) {
        this.refereeLicenceRepository = refereeLicenceRepository;
        this.refereeRepository = refereeRepository;
    }

    @Transactional
    public RefereeCreateLicenceResponseDto addRefereeLicence(RefereeCreateLicenceRequestDto requestDto) {

        // TODO VERIFY REFEREE LICENCE

        Referee referee = refereeRepository.getReferenceById(requestDto.userId());
        RefereeLicence refereeLicence = requestDto.toModel(referee);

        refereeLicenceRepository.save(refereeLicence);
        return RefereeCreateLicenceResponseDto.fromModel(refereeLicence);
    }

    public List<RefereeLicenceResponseDto> getRefereeLicencesByRefereeId(Integer refereeId) {
        return refereeLicenceRepository.findByRefereeUserId(refereeId)
                .stream()
                .map(RefereeLicenceResponseDto::fromModel)
                .toList();
    }

    public RefereeLicenceResponseDto getRefereeLicenceByLicense(String license) {
        RefereeLicence refereeLicence = refereeLicenceRepository.findByLicense(license);
        return RefereeLicenceResponseDto.fromModel(refereeLicence);
    }
}
