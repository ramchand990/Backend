package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoggedInUserClaimData implements Comparable<LoggedInUserClaimData> {

    private Long claimID;
    private Long claimStageLinkId;
    private String name;
    private String ailment;
    private String stage;
    private String status;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
    private Long ageing;
    private double approvedAmount;
    private LocalDateTime createdDateTime;
    private ClaimSlaDto consumedSla;

    @Override
    public int compareTo(LoggedInUserClaimData o) {
        if (o.getCreatedDateTime().isAfter(this.getCreatedDateTime()))
            return 1;
        if (o.getCreatedDateTime().isBefore(this.getCreatedDateTime()))
            return -1;
        else
            return 0;
    }
}
