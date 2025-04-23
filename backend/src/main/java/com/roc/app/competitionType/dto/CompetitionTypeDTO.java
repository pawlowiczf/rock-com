package com.roc.app.competitionType.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionTypeDTO {

    private Integer typeId;

    @NotBlank(message = "Type label is required")
    @Size(max = 20, message = "Type label must not exceed 20 characters")
    private String typeLabel;




}
