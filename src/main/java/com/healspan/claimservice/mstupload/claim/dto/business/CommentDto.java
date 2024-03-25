package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private long claimId;
    private long stageId;
    private AssignmentFlowType flowType;
    private String transferComment;
    private List<Long> documentIds;
}