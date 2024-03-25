package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClaimStagePendingCount {

    private int initialAuthorizationCount=0;
    private int enhancementCount=0;
    private int dischargeCount=0;
    private int finalClaimCount=0;
    private List<LoggedInUserClaimData> pendingList;
}
