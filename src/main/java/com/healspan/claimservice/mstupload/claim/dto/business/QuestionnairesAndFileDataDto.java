package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionnairesAndFileDataDto {

    private Long claimStageLinkId;
    private List<QuestionAnswerDto> sequentialQuestion;
    private List<Long> documentIdList;
}
