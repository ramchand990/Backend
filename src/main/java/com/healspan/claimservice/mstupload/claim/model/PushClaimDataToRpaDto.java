package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PushClaimDataToRpaDto {
    private long claimId;
    private long stageId;
    private String transferComment;
    private AssignmentFlowType flowType;
}
