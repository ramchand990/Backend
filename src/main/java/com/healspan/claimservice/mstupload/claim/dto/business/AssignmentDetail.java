package com.healspan.claimservice.mstupload.claim.dto.business;

import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimInfo;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AssignmentDetail {
    private ClaimInfo claimInfo;
    private ClaimStageMst claimStage;
    private StatusMst currentStatus;
    private ClaimStageLink claimStageLink;
    private AssignmentFlowType flowType;
    private UserMst lastOwner;
    private UserMst nextUserToBeAssigned;
    private StatusMst nextStatusToBeAssigned;
    private String comments;
    private long iterationInstance;
    private Map<String,UserMst> usersWorkOnClaim;
    private List<ClaimAssignment> claimAssignmentsList;
}
