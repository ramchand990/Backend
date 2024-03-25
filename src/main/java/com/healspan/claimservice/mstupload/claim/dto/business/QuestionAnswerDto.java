package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionAnswerDto {

    private Long id;
    private String question;
    private String answer;
}