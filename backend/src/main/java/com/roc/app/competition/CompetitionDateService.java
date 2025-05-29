package com.roc.app.competition;


import com.roc.app.competition.dto.CompetitionDateCreateRequestDto;
import com.roc.app.competition.dto.CompetitionDateResponseDto;
import com.roc.app.competition.dto.CompetitionDateUpdateRequestDto;
import com.roc.app.competition.exception.CompetitionDateNotFoundException;
import com.roc.app.competition.exception.CompetitionNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

    public void updateDate(Integer competitionDateId, CompetitionDateUpdateRequestDto competitionDateUpdateRequestDto) {
        CompetitionDate competitionDate = dateRepository.findById(competitionDateId)
                .orElseThrow(() -> new CompetitionDateNotFoundException(competitionDateId));

        competitionDate.setStartTime(competitionDateUpdateRequestDto.startTime());
        competitionDate.setEndTime(competitionDateUpdateRequestDto.endTime());
        dateRepository.save(competitionDate);
    }

    public void deleteDate(Integer competitionDateId) {
        CompetitionDate competitionDate = dateRepository.findById(competitionDateId)
                .orElseThrow(() -> new CompetitionDateNotFoundException(competitionDateId));
        dateRepository.delete(competitionDate);
    }


    public List<CompetitionDateResponseDto> getCompetitionDates(Integer competitionId) {
        List<CompetitionDate> competitionDates = dateRepository.findAllByCompetitionId(competitionId);
        return competitionDates.stream().map(CompetitionDateResponseDto::fromModel).collect(Collectors.toList());
    }

}
