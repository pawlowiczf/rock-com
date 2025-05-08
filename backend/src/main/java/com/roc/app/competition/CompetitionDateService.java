package com.roc.app.competition;


import com.roc.app.competition.dto.CompetitionDateCreateRequestDto;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CompetitionDateService {

    private final CompetitionDateRepository dateRepository;
    private final CompetitionRepository competitionRepository;
    private final CompetitionDateMapper competitionDateMapper;

    @Transactional
    public void addDatesToCompetition(List<CompetitionDateCreateRequestDto> competitionDateCreateRequestDtos) {
            competitionDateCreateRequestDtos.forEach(competitionDateCreateRequestDto -> {
                                Competition competition = competitionRepository.findById(competitionDateCreateRequestDto.competitionId())
                                        .orElseThrow(() -> new CompetitionNotFoundException(competitionDateCreateRequestDto.competitionId()));
                                CompetitionDate competitionDate = competitionDateMapper.mapToEntity(competitionDateCreateRequestDto, competition);
                                dateRepository.save(competitionDate);
                            });
    }
}
