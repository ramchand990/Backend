package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.dto.business.*;
import com.healspan.claimservice.mstupload.claim.model.*;

public interface ClaimOperationService {

    ResponseClaimStatusDto createClaimAsDraft(ClaimInfoDto dto);
    DashboardDataDto findAllClaimCreatedByUser(Long userId);
    String getHospitalUserDashboardData(long userId);

    String getHealspanUserDashboardData(long userId);
    ReviewerDashboardDataDto findAllReviewerClaims(Long userId);
    ClaimStageLinkDto findClaim(Long id);
    ResourceCreationResponse createOrUpdateClaimInfo(ClaimInfoDto dto);
    ResourceCreationResponse createOrUpdateMedicalInfo(MedicalInfoDto dto);
    ResourceCreationResponse createOrUpdateInsuranceInfo(InsuranceInfoDto dto);
    ResourceCreationResponse createOrUpdateQuestionnairesAndFileData(QuestionnairesAndFileDataDto dto);
    DashboardDataDto findAllClaimCreatedByUserAndName(Long userId,String name);
    boolean updateClaimStatus(UpdateClaimStatusDto req);
    ResponseClaimStatusDto pushClaimDataToRpa(PushClaimDataToRpaDto dto);
    ResponseClaimStatusDto updateStage(RequestDto dto);

    OtherDocumentDto findDocumentId(String docName, Long id);
    ReviewerDashboardDataDto getClaimsForReviewerDashboard(long loggedInUserId);
    public DocumentData deleteDocument(long documentId);
}
