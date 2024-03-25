package com.healspan.claimservice.mstupload.claim.controller;

import com.google.gson.Gson;
import com.healspan.claimservice.mstupload.claim.constants.Constant;
import com.healspan.claimservice.mstupload.claim.dto.business.*;
import com.healspan.claimservice.mstupload.claim.model.*;
import com.healspan.claimservice.mstupload.claim.service.ClaimAssignmentService;
import com.healspan.claimservice.mstupload.claim.service.ClaimOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/healspan/claim")
public class ClaimController {

    private final Logger logger = LoggerFactory.getLogger(ClaimController.class);

    @Autowired
    private ClaimOperationService claimOperationService;

    private static final String deleteMessage = "Document details has been deleted for requested id:";

    @Autowired
    private ClaimAssignmentService claimAssignmentService;

    @Autowired
    private Gson gson;

    @PostMapping("/createclaimwithallentities")
    public ResponseEntity<ResponseClaimStatusDto> createClaimAndPatientInfo(@RequestBody ClaimInfoDto dto) {
        logger.info("Request received to create claim and patient info for claimData: {}", dto);
        return new ResponseEntity<>(claimOperationService.createClaimAsDraft(dto), HttpStatus.OK);
    }

    //for hospital user
    @GetMapping("/retrieveallclaimsofloggedinuser/{userId}")
    public ResponseEntity<DashboardDataDto> retrieveAllClaimOfLoggedInUser(@PathVariable Long userId) {
        logger.info("Request received to retrieve all claim of logged in user for userId:{}", userId);
        DashboardDataDto dashboardDataDto = claimOperationService.findAllClaimCreatedByUser(userId);
        if (dashboardDataDto != null) {
            return new ResponseEntity<>(dashboardDataDto, HttpStatus.OK);
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

    }

    @GetMapping(value = "/hospitaluserdashboarddata/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getHospitalUserDashboardData(@PathVariable long userId) {
        logger.info("ClaimController::getHospitalUserDashboardData::userId::{}", userId);
        return new ResponseEntity<>(claimOperationService.getHospitalUserDashboardData(userId), HttpStatus.OK);
    }
    @GetMapping(value = "/healspanuserdashboarddata/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getHealspanUserDashboardData(@PathVariable long userId) {
        logger.info("ClaimController::getHealspanUserDashboardData::userId::{}", userId);
        return new ResponseEntity<>(claimOperationService.getHealspanUserDashboardData(userId), HttpStatus.OK);
    }

    @GetMapping("/retrieveallclaimsofloggedinuser/{userId}/input/{searchBY}")
    public ResponseEntity<DashboardDataDto> retrieveAllClaimOfLoggedInUser(@PathVariable Long userId
            , @PathVariable String searchBY) {
        logger.info("Request received to retrieve all claim of logged in user for userId:{} and searchBy:{}", userId, searchBY);
        return new ResponseEntity<>(claimOperationService.findAllClaimCreatedByUserAndName(userId, searchBY), HttpStatus.OK);
    }

    @GetMapping("/reviewer-claims/{userId}")
    public ResponseEntity<ReviewerDashboardDataDto> retrieveAllClaimsOfReviewer(@PathVariable Long userId) {
        logger.info("Request received to retrieve all claims of reviewer for userId:{}", userId);
        return new ResponseEntity<>(claimOperationService.findAllReviewerClaims(userId), HttpStatus.OK);
    }

    @GetMapping("/retrieveclaim/{claimStageLinkId}")
    public ResponseEntity<ClaimStageLinkDto> retrieveClaimInfo(@PathVariable Long claimStageLinkId) {
        logger.info("Request received to retrieve claim info for claimStageLinkId:{}", claimStageLinkId);
        return new ResponseEntity<>(claimOperationService.findClaim(claimStageLinkId), HttpStatus.OK);
    }

    @PostMapping("/createorupdateclaimandpatientinfo")
    public ResponseEntity<ResourceCreationResponse> createOrUpdateClaimInfo(@RequestBody ClaimInfoDto dto) {
        logger.info("Request received to create or update claim info for claimId:{}", dto.getId());
        return new ResponseEntity<>(claimOperationService.createOrUpdateClaimInfo(dto), HttpStatus.OK);
    }

    @PostMapping("/createorupdatemedicalinfo")
    public ResponseEntity<ResourceCreationResponse> createOrUpdateMedicalInfo(@RequestBody MedicalInfoDto dto) {
        logger.info("Request received to create or update medical info for claimStageLinkId:{} and claimStageId:{}", dto.getClaimStageLinkId(), dto.getClaimStageId());
        return new ResponseEntity<>(claimOperationService.createOrUpdateMedicalInfo(dto), HttpStatus.OK);
    }

    @PostMapping("/createorupdateinsuranceinfo")
    public ResponseEntity<ResourceCreationResponse> createOrUpdateInsuranceInfo(@RequestBody InsuranceInfoDto dto) {
        logger.info("Request received to create or update insurance info for ClaimID:{} and claimStageID:{}", dto.getClaimInfoId(), dto.getClaimStageId());
        return new ResponseEntity<>(claimOperationService.createOrUpdateInsuranceInfo(dto), HttpStatus.OK);
    }

    @PostMapping("/savequestionnairesanddocument")
    public ResponseEntity<ResourceCreationResponse> saveQuestionnairesAndDocument(@RequestBody QuestionnairesAndFileDataDto dto) {
        logger.info("Request received to save questionnaires and document for medicalId:{}", dto.getClaimStageLinkId());
        return new ResponseEntity<>(claimOperationService.createOrUpdateQuestionnairesAndFileData(dto), HttpStatus.OK);
    }

    //  //Assign the created claim to reviewer
    @PostMapping("/updateclaimstatus")
    public ResponseEntity<ResponseClaimStatusDto> updateClaimStatus(@RequestBody UpdateClaimStatusDto req) {
        logger.info("ClaimController/updateclaimstatus::request::{}", gson.toJson(req));
        ResponseClaimStatusDto response = new ResponseClaimStatusDto();
        logger.info("Request received to update the claim status for ClaimId:{}", req.getClaimId());
        boolean assignmentStatus = claimOperationService.updateClaimStatus(req);
        logger.info("ClaimController::UpdateClaimStatus::ClaimInfoId::{}::AssignmentStatus::{}", req.getClaimId(), assignmentStatus);
        if (assignmentStatus) {
            logger.info("Request processed successfully to update the status for ClaimId: {}", req.getClaimId());
            response.setResponseStatus(String.valueOf(Constant.ResponseStatus.SUCCESS));
            response.setClaimId(req.getClaimId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            logger.info("FAILED to update the status for ClaimId: {}", req.getClaimId());
            response.setResponseStatus(String.valueOf(Constant.ResponseStatus.FAILED));
            response.setClaimId(req.getClaimId());
            return new ResponseEntity<>(response, HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping("/comment")
    public ResponseEntity<ResponseClaimStatusDto> assignedCommentProcess(@RequestBody CommentDto dto) {
        logger.info("ClaimController/comment::request::{}", gson.toJson(dto));
        logger.info("Request received to assigned comment process for reviewerId{}", dto.getClaimId());
        return new ResponseEntity<>(claimAssignmentService.assignedCommentProcess(dto), HttpStatus.OK);
    }

    @PostMapping("/pushclaimdatatorpa")
    public ResponseEntity<ResponseClaimStatusDto> pushClaimDataToRpa(@RequestBody PushClaimDataToRpaDto dto) {
        logger.info("Request received to push claim data to rpa for claimId:{} and stageId:{}", dto.getClaimId(), dto.getStageId());
        return new ResponseEntity<>(claimOperationService.pushClaimDataToRpa(dto), HttpStatus.OK);
    }

    @PostMapping("/updatestage")
    public ResponseEntity<ResponseClaimStatusDto> updateStage(@RequestBody RequestDto dto) {
        return new ResponseEntity<>(claimOperationService.updateStage(dto), HttpStatus.OK);
    }

    @GetMapping("/getdocument/{docName}/{id}")
    public ResponseEntity<OtherDocumentDto> findDocumentById(@PathVariable String docName, @PathVariable Long id) {
        return new ResponseEntity<>(claimOperationService.findDocumentId(docName, id), HttpStatus.OK);
    }

    @GetMapping("/getreviewerdashboardclaims/{loggedInUserId}")
    public ResponseEntity<ReviewerDashboardDataDto> getClaimsForReviewerDashboard(@PathVariable Long loggedInUserId) {
        logger.info("Request received for get claims for reviewer dashboard for loggedInUserId:{}",loggedInUserId);
        return new ResponseEntity<>(claimOperationService.getClaimsForReviewerDashboard(loggedInUserId), HttpStatus.OK);
    }

    @DeleteMapping("/delete-documents/{documentId}")
    public ResponseEntity<DocumentData> deleteDocument(@PathVariable long documentId) {
        logger.info("Request received for delete document for documentId:{}",documentId);
        return new ResponseEntity<>(claimOperationService.deleteDocument(documentId), HttpStatus.OK);
    }
}