package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ClaimStatus;
import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;

@Getter
@Setter
@NoArgsConstructor
public class TpaResponseDto{

    private long claimId;
    private long stageId;
    private String claimNumber;
    private String transferComment;
    private ClaimStatus status;
    private double approvedAmount;

    @Override
    public String toString() {
        return "TpaResponseDto{" +
                "claimId=" + claimId +
                ", stageId=" + stageId +
                ", claimNumber='" + claimNumber + '\'' +
                ", remarks='" + transferComment + '\'' +
                ", status=" + status +
                ", approvedAmount=" + approvedAmount +
                '}';
    }
}
