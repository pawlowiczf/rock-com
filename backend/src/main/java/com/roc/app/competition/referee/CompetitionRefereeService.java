package com.roc.app.competition.referee;

import com.roc.app.competition.Competition;
import com.roc.app.competition.CompetitionRepository;
import com.roc.app.competition.referee.dto.CompetitionRefereeResponseDto;
import com.roc.app.user.referee.general.Referee;
import com.roc.app.user.referee.licence.RefereeLicence;
import com.roc.app.user.referee.licence.RefereeLicenceService;
import com.roc.app.user.referee.licence.dto.RefereeLicenceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class CompetitionRefereeService {
    private final CompetitionRefereeRepository competitionRefereeRepository;
    private final CompetitionRepository competitionRepository;

    private final RefereeLicenceService refereeLicenceService;

    public CompetitionRefereeService(CompetitionRefereeRepository competitionRefereeRepository, CompetitionRepository competitionRepository, RefereeLicenceService refereeLicenceService) {
        this.competitionRefereeRepository = competitionRefereeRepository;
        this.competitionRepository = competitionRepository;
        this.refereeLicenceService = refereeLicenceService;
    }

    CompetitionRefereeResponseDto assignRefereeToCompetition(Competition competition, Referee referee) {

        if (!validateRefereeLicenceType(competition, referee)) {
            throw new RefereeNotEligibleException(referee.getUserId());
        }

        CompetitionReferee model = CompetitionReferee.builder()
                .competitionId(competition.getCompetitionId())
                .refereeId(referee.getUserId())
                .build();


        competitionRefereeRepository.save(model);
        return CompetitionRefereeResponseDto.fromModel(model);
    }

    List<CompetitionRefereeResponseDto> listCompetitionReferees(Competition competition) {
        return competitionRefereeRepository.findByCompetitionId(competition.getCompetitionId())
                .stream()
                .map(CompetitionRefereeResponseDto::fromModel)
                .toList();
    }

    private boolean validateRefereeLicenceType(Competition competition, Referee referee) {
        List<RefereeLicenceResponseDto> refereeLicence = refereeLicenceService.getRefereeLicencesByRefereeId(referee.getUserId());

        for (RefereeLicenceResponseDto licence : refereeLicence) {
            if (licence.licenceType() == competition.getType()) {
                return true;
            }
        }

        return false;
    }
}
