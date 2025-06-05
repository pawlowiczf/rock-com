package com.roc.app.match.dto;

import jakarta.validation.constraints.NotNull;

public record ScoreCreateRequestDto (
        @NotNull Integer player1,
        @NotNull Integer player2
){
    public String getScore(){
        return player1 + ":" + player2;
    }

    public static ScoreCreateRequestDto toDto(String score){
        String[] points = score.split(":");
        return new ScoreCreateRequestDto(
                Integer.parseInt(points[0]),
                Integer.parseInt(points[1])
        );
    }
}
