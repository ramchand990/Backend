package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimInfo;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dto.business.ClaimInfoDto;
import com.healspan.claimservice.mstupload.claim.model.ClaimStageApprovalCount;
import com.healspan.claimservice.mstupload.claim.model.ClaimStagePendingCount;
import com.healspan.claimservice.mstupload.claim.model.LoggedInUserClaimData;
import com.healspan.claimservice.mstupload.claim.model.ReviewerClaimsData;
import com.healspan.claimservice.mstupload.claim.repo.ClaimInfoRepo;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import com.healspan.claimservice.mstupload.repo.HospitalMstRepo;
import com.healspan.claimservice.mstupload.repo.UserMstRepo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClaimUtil {
    private Logger logger = LoggerFactory.getLogger(ClaimUtil.class);

    @Autowired
    private ClaimInfoRepo claimInfoRepo;
    @Autowired
    private HospitalMstRepo hospitalMstRepo;
    @Autowired
    private UserMstRepo userMstRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;

    public ClaimInfo saveOrUpdateClaim(ClaimInfoDto dto){
        logger.debug("ClaimUtil::saveOrUpdateClaim requestedData: {} --Start",dto);
        ClaimInfo claimInfoDao = modelMapper.map(dto, ClaimInfo.class);
        if(dto.getId()!=null && dto.getClaimStageLinkId()!=null){
            claimInfoDao.setId(dto.getId());
        }
        claimInfoDao.setCreatedDateTime(LocalDateTime.now());
        logger.debug("Find the hospital record from db for hospitalId:{} and set to claimInfoDao",dto.getHospitalId());
        claimInfoDao.setHospitalMst(hospitalMstRepo.findById(dto.getHospitalId()).get());
        logger.debug("Find the user record from db for userId:{} and set to claimInfoDao",dto.getUserId());
        claimInfoDao.setUserMst(userMstRepo.findById(dto.getUserId()).get());
        logger.debug("Save Claim Info record in db for claimInfoId:{}",dto.getUserId());
        claimInfoRepo.save(claimInfoDao);
        logger.debug("ClaimUtil::saveOrUpdateClaim responseData: {} --End",claimInfoDao);
        return claimInfoDao;
    }

    public List<ReviewerClaimsData> getAllReviewerClaims(Long userId) {
        logger.debug("ClaimUtil::getAllReviewerClaims requestedUserId: {} --Start",userId);
        List<ReviewerClaimsData> reviewerClaimsData = new ArrayList<>();
        try{
            logger.debug("Find all claim stage link id records from db for userId:{}",userId);
            List<Long> claimStageLinkIdList = claimStageLinkRepo.findAllByUserMstId(userId);
            logger.debug("Find all reviewerUserId records from db for reviewerUserId:{} and add all reviewerUserId to claimStageLinkIdList:{}",userId,claimStageLinkIdList);
            claimStageLinkIdList.addAll(claimStageLinkRepo.findAllByReviewerUserMstId(userId));

            //Get claim details for all the Ids
            logger.debug("Find all claim stage link records from db for all ClaimStageLinkIdList:{}",claimStageLinkIdList);
            List<ClaimStageLink> claimInfoDaoList = claimStageLinkRepo.findAllByIdIn(claimStageLinkIdList) ;
            claimInfoDaoList.forEach(obj->{
                ReviewerClaimsData dto = new ReviewerClaimsData();
                dto.setClaimID(obj.getClaimInfo().getId());
                dto.setClaimStageLinkId(obj.getId());
                dto.setName(obj.getPatientInfo().getFirstName()+" "+obj.getPatientInfo().getMiddleName()+" "+obj.getPatientInfo().getLastname());

                if(obj.getMedicalInfo()!=null) {
                    dto.setAilment(obj.getMedicalInfo().getDiagnosisMst().getName());
                }
                dto.setStage(obj.getClaimStageMst().getName());
                dto.setStatus(obj.getStatusMst().getName());
                dto.setPTat("-");
                dto.setApprovedAmount(obj.getPatientInfo().getClaimedAmount());
                dto.setHospital(obj.getClaimInfo().getHospitalMst().getName());
                dto.setCreatedDateTime(obj.getCreatedDateTime());
                reviewerClaimsData.add(dto);
            });
        }catch (Exception e){
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
        }
        Collections.sort(reviewerClaimsData);
        logger.debug("ClaimUtil::getAllReviewerClaims responseData: {} --End",reviewerClaimsData);
        return reviewerClaimsData;
    }

    public List<LoggedInUserClaimData> findAllClaimCreatedByUser(Long userId) {
        logger.debug("ClaimUtil::findAllClaimCreatedByUser requestedUserId: {} --Start",userId);
        List<LoggedInUserClaimData> loggedInUserClaimDataList = new ArrayList<>();
        try{
            logger.debug("Find all claim stage link id records from db for userId: {}",userId);
            List<ClaimInfo> claimInfoList = claimInfoRepo.findAllByUserMstId(userId);
            List<Long> claimIdList = claimInfoList.stream().map(obj->obj.getId()).collect(Collectors.toList());
            List<Long> claimStageLinkIdList = claimStageLinkRepo.findAllByClaimIdList(claimIdList);
            logger.debug("Find all claim stage link records for all claimStageLinkedIdList: {}",claimStageLinkIdList);
            List<ClaimStageLink> claimInfoDaoList = claimStageLinkRepo.findAllByIdIn(claimStageLinkIdList) ;
            for(ClaimStageLink claimStageLinkDao : claimInfoDaoList){
                try {
                    LoggedInUserClaimData dto = new LoggedInUserClaimData();
                    dto.setClaimID(claimStageLinkDao.getClaimInfo().getId());
                    dto.setClaimStageLinkId(claimStageLinkDao.getId());
                    dto.setName(claimStageLinkDao.getPatientInfo().getFirstName() + " " + claimStageLinkDao.getPatientInfo().getMiddleName() + " " + claimStageLinkDao.getPatientInfo().getLastname());
                    if (claimStageLinkDao.getMedicalInfo() != null) {
                        dto.setAilment(claimStageLinkDao.getMedicalInfo().getDiagnosisMst().getName());
                    }
                    dto.setStage(claimStageLinkDao.getClaimStageMst().getName());
                    dto.setStatus(claimStageLinkDao.getStatusMst().getName());
                    dto.setAdmissionDate(claimStageLinkDao.getPatientInfo().getDateOfAdmission());
                    dto.setDischargeDate(claimStageLinkDao.getPatientInfo().getDateOfDischarge());
                    Long ageing = Duration.between(claimStageLinkDao.getCreatedDateTime(), LocalDateTime.now()).toDays();
                    dto.setAgeing(ageing);
                    dto.setApprovedAmount(claimStageLinkDao.getPatientInfo().getClaimedAmount());
                    dto.setCreatedDateTime(claimStageLinkDao.getCreatedDateTime());
                    loggedInUserClaimDataList.add(dto);
                }catch (Exception e){
                    logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
                }
            }
        }catch (Exception e){
            logger.error("Exception Details: {}",ExceptionUtils.getStackTrace(e));
        }
        Collections.sort(loggedInUserClaimDataList);
        logger.debug("ClaimUtil::findAllClaimCreatedByUser responseData: {} --End",loggedInUserClaimDataList);
        return loggedInUserClaimDataList;
    }

    public ClaimStagePendingCount deriveClaimStageDraftData(List<LoggedInUserClaimData> dtoList) {
        logger.info("ClaimUtil::deriveClaimStageDraftData requestedData: {} --Start",dtoList);
        ClaimStagePendingCount draftData = new ClaimStagePendingCount();
        List<LoggedInUserClaimData> pendingList = new ArrayList<>();
        int initialAuthorizationCount = 0;
        int enhancementCount = 0;
        int dischargeCount = 0;
        int finalClaimCount = 0;
        if(!dtoList.isEmpty()) {
            for (LoggedInUserClaimData dto : dtoList) {
                if ("Initial Authorization".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending Documents")) {
                    pendingList.add(dto);
                    initialAuthorizationCount++;
                } else if ("Enhancement".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending Documents")) {
                    pendingList.add(dto);
                    enhancementCount++;
                } else if ("Discharge".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending Documents")) {
                    pendingList.add(dto);
                    dischargeCount++;
                } else if ("Final Claim".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending Documents")) {
                    pendingList.add(dto);
                    finalClaimCount++;
                }
            }
                draftData.setFinalClaimCount(finalClaimCount);
                draftData.setDischargeCount(dischargeCount);
                draftData.setEnhancementCount(enhancementCount);
                draftData.setInitialAuthorizationCount(initialAuthorizationCount);
                draftData.setPendingList(pendingList);
        }else {
            draftData.setFinalClaimCount(finalClaimCount);
            draftData.setDischargeCount(dischargeCount);
            draftData.setEnhancementCount(enhancementCount);
            draftData.setInitialAuthorizationCount(initialAuthorizationCount);
            draftData.setPendingList(pendingList);
        }
        logger.info("ClaimUtil::deriveClaimStageDraftData responseData: {} --End",draftData);
        return draftData;
    }

    public ClaimStageApprovalCount deriveClaimStageApprovalData(List<LoggedInUserClaimData> dtoList) {
        logger.info("ClaimUtil::deriveClaimStageApprovalData requestedData: {} --Start",dtoList);
        ClaimStageApprovalCount approvalData = new ClaimStageApprovalCount();
        List<LoggedInUserClaimData> approvalList = new ArrayList<>();
        int initialAuthorizationCount = 0;
        int enhancementCount = 0;
        int dischargeCount = 0;
        int finalClaimCount = 0;
        if(!dtoList.isEmpty()) {
        for (LoggedInUserClaimData dto : dtoList) {
            if ("Initial Authorization".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending HS Approval")) {
                approvalList.add(dto);
                initialAuthorizationCount++;
            } else if ("Enhancement".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending HS Approval")) {
                approvalList.add(dto);
                enhancementCount++;
            } else if ("Discharge".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending HS Approval")) {
                approvalList.add(dto);
                dischargeCount++;
            } else if ("Final Claim".equalsIgnoreCase(dto.getStage()) && dto.getStatus().equalsIgnoreCase("Pending HS Approval")) {
                approvalList.add(dto);
                finalClaimCount++;
            }
        }
            approvalData.setFinalClaimCount(finalClaimCount);
            approvalData.setDischargeCount(dischargeCount);
            approvalData.setEnhancementCount(enhancementCount);
            approvalData.setInitialAuthorizationCount(initialAuthorizationCount);
            approvalData.setApprovalList(approvalList);
        }
        else {
            approvalData.setFinalClaimCount(finalClaimCount);
            approvalData.setDischargeCount(dischargeCount);
            approvalData.setEnhancementCount(enhancementCount);
            approvalData.setInitialAuthorizationCount(initialAuthorizationCount);
            approvalData.setApprovalList(approvalList);
        }
        logger.info("ClaimUtil::deriveClaimStageApprovalData responseData: {} --End",approvalData);
        return approvalData;
    }

    public List<LoggedInUserClaimData> findAllClaimCreatedByUserAndName(List<ClaimStageLink> claimInfoDaoList,Long claimId,String name) {
        return createDashboardDto(claimInfoDaoList,claimId, name);
    }

    private List<LoggedInUserClaimData> createDashboardDto(List<ClaimStageLink> claimInfoDaoList,Long claimId, String name) {
        logger.debug("ClaimUtil::createDashboardDto requestedClaimInfoDaoList: {},claimId: {} and name: {} --Start",claimInfoDaoList,claimId,name);
        List<LoggedInUserClaimData> loggedInUserClaimDataList = new ArrayList<>();
        for (ClaimStageLink obj: claimInfoDaoList) {
            LoggedInUserClaimData dto = new LoggedInUserClaimData();
            dto.setClaimID(obj.getClaimInfo().getId());
            dto.setClaimStageLinkId(obj.getId());
            dto.setName(obj.getPatientInfo().getFirstName()+" "+obj.getPatientInfo().getMiddleName()+" "+obj.getPatientInfo().getLastname());
            if(obj.getMedicalInfo()!=null) {
                dto.setAilment(obj.getMedicalInfo().getDiagnosisMst().getName());
            }
            dto.setStage(obj.getClaimStageMst().getName());
            dto.setStatus(obj.getStatusMst().getName());
            dto.setAdmissionDate(obj.getPatientInfo().getDateOfAdmission());
            dto.setDischargeDate(obj.getPatientInfo().getDateOfDischarge());
            dto.setAgeing(null);
            dto.setApprovedAmount(obj.getPatientInfo().getClaimedAmount());
            if(name != null && name.equalsIgnoreCase(obj.getPatientInfo().getFirstName()) ) {
                loggedInUserClaimDataList.add(dto);
            }else if(claimId != null)
                loggedInUserClaimDataList.add(dto);
        }
        logger.debug("ClaimUtil::createDashboardDto responseData: {} --End",loggedInUserClaimDataList);
        return loggedInUserClaimDataList;
    }
}
