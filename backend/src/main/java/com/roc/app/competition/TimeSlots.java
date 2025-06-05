package com.roc.app.competition;

import com.roc.app.competition.dto.CompetitionDateResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

@Getter
public class TimeSlots {
    private final TreeMap<LocalDateTime, Integer> slots;

    public TimeSlots(Competition competition, List<CompetitionDateResponseDto> dates) {
        // TODO: available or referees number
        this.slots = new TreeMap<>();
        for (CompetitionDateResponseDto date : dates) {
            LocalDateTime current = date.startTime();
            while (current.plusMinutes(competition.getMatchDurationMinutes()).isBefore(date.endTime()) ||
                    current.plusMinutes(competition.getMatchDurationMinutes()).isEqual(date.endTime())) {
                slots.put(current, competition.getAvailableCourts());
                current = current.plusMinutes(competition.getMatchDurationMinutes());
            }
        }
    }

    public LocalDateTime getNext(){
        LocalDateTime timeSlot = slots.firstKey();
        int slotsLeft = slots.get(timeSlot) - 1;
        if (slotsLeft == 0){
            slots.remove(timeSlot);
        } else {
            slots.put(timeSlot, slotsLeft);
        }
        return timeSlot;
    }

    public void removeSlot(LocalDateTime slot){
        slots.remove(slot);
    }

}
