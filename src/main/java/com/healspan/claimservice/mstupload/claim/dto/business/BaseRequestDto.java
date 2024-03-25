package com.healspan.claimservice.mstupload.claim.dto.business;

import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BaseRequestDto {
    private long claimId;
    private long stageId;
    private AssignmentFlowType flowType;
    private String transferComment;
}