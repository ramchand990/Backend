package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.dto.business.AssignmentDetail;
import com.healspan.claimservice.mstupload.claim.dto.business.BaseRequestDto;

public interface UpdateClaimAssignmentService {

    AssignmentDetail prepareAssignmentDetail(BaseRequestDto baseRequestDto);
    void setNewUserAndStatus(AssignmentDetail detail);
    void insertCommunicationDetailsInCaseAssignment(AssignmentDetail detail);
}