package com.roc.app.match.dto;

import jakarta.validation.constraints.NotNull;

public record ScoreCreateRequestDto (
        @NotNull Integer player1,
        @NotNull Integer player2
){
    public String getScore(){
        return player1 + ":" + player2;
    }
}
