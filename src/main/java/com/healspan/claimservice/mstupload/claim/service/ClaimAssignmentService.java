package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dto.business.CommentDto;
import com.healspan.claimservice.mstupload.claim.dto.business.ResponseClaimStatusDto;
import com.healspan.claimservice.mstupload.claim.dto.business.UpdatedDetailsForClaimAssignment;

public interface ClaimAssignmentService {

    ClaimAssignment assignClaim(ClaimStageLink claimStageLink, UpdatedDetailsForClaimAssignment detailsForClaimAssignment);
    ResponseClaimStatusDto assignedCommentProcess(CommentDto dto);
}