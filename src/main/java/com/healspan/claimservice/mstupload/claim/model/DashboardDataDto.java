package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DashboardDataDto {
    private ClaimStagePendingCount claimStagePendingCount;
    private ClaimStageApprovalCount claimStageApprovalCount;

}
