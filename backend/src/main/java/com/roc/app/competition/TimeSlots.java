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
        Integer matchDuration = competition.getMatchDurationMinutes();
        for (CompetitionDateResponseDto date : dates) {
            LocalDateTime slotStartTime = date.startTime();
            while (!slotStartTime.plusMinutes(matchDuration).isAfter(date.endTime())) {
                slots.put(slotStartTime, competition.getAvailableCourts());
                slotStartTime = slotStartTime.plusMinutes(matchDuration);
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
