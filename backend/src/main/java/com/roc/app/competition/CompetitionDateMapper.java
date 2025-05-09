package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDateCreateRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CompetitionDateMapper {

    CompetitionDate mapToEntity(CompetitionDateCreateRequestDto competitionDateCreateRequestDto,
                                Competition competition){
        return CompetitionDate.builder()
                .competition(competition)
                .startTime(competitionDateCreateRequestDto.startTime())
                .endTime(competitionDateCreateRequestDto.endTime()).build();
    }
}
