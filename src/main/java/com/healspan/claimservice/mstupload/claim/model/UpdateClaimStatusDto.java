package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;

@Getter
@Setter
@NoArgsConstructor
public class UpdateClaimStatusDto {
    private long claimId;
    private long stageId;
    private AssignmentFlowType flowType;
    private String transferComment;
}
