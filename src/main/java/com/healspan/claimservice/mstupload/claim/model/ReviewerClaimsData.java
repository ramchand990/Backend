package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReviewerClaimsData implements Comparable<ReviewerClaimsData> {

    private Long claimID;
    private Long claimStageLinkId;
    private String name;
    private String ailment;
    private String pTat;
    private String stage;
    private String status;
    private String hospital;
    private double approvedAmount;
    private LocalDateTime createdDateTime;
    private Date createdDate;
    private long slaPercent;

    @Override
    public int compareTo(ReviewerClaimsData r) {
        if (r.getCreatedDateTime().isAfter(this.getCreatedDateTime()))
            return 1;
        if (r.getCreatedDateTime().isBefore(this.getCreatedDateTime()))
            return -1;
        else
            return 0;
    }
}