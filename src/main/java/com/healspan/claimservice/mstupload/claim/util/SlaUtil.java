package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.initialize.MasterStore;
import com.healspan.claimservice.mstupload.claim.model.ClaimSlaDto;
import com.healspan.claimservice.mstupload.claim.model.LoggedInUserClaimData;
import com.healspan.claimservice.mstupload.claim.repo.ClaimAssignmentRepo;
import com.healspan.claimservice.util.JwtUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ClaimStage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SlaUtil {

    @Autowired
    private ClaimAssignmentRepo claimAssignmentRepo;

    @Autowired
    private MasterStore masterStore;

    private Logger logger = LoggerFactory.getLogger(SlaUtil.class);

    public Map<Long, ClaimSlaDto> findAssignmentTransactionsToDeriveSlaAgainstAllClaims(List<LoggedInUserClaimData> loggedInUserClaimDataList) {
        logger.info("SlaUtil::findAssignmentTransactionsToDeriveSlaAgainstAllClaims::DateTime::{}::START" , LocalDateTime.now());
        Map<Long, ClaimSlaDto> slaMap = null;
        try {
            List<Long> claimIdList = loggedInUserClaimDataList.stream().map(object -> object.getClaimID()).collect(Collectors.toList());
            List<ClaimAssignment> claimAssignmentList = claimAssignmentRepo.findAllByClaimInfoIdIn(claimIdList);
            System.out.println(claimIdList);
            slaMap = iterateClaimsToDeriveSlaOfEachClaim(loggedInUserClaimDataList, claimAssignmentList);
            logger.info("SlaUtil::findAssignmentTransactionsToDeriveSlaAgainstAllClaims::DateTime::{}::END" , LocalDateTime.now());
        } catch (Exception e) {
            logger.error("SlaUtil::findAssignmentTransactionsToDeriveSlaAgainstAllClaims::Exception::{}" , ExceptionUtils.getStackTrace(e));
        }
        return slaMap;
    }

    private Map<Long, ClaimSlaDto> iterateClaimsToDeriveSlaOfEachClaim(List<LoggedInUserClaimData> loggedInUserClaimDataList,
                                                                       List<ClaimAssignment> claimAssignmentList) {
        List<ClaimAssignment> initialAuthorizationStageTransactionsOfCurrentClaim = null;
        List<ClaimAssignment> enhancementStageTransactionsOfCurrentClaim = null;
        List<ClaimAssignment> dischargeStageTransactionsOfCurrentClaim = null;
        List<ClaimAssignment> finalClaimStageTransactionsOfCurrentClaim = null;
        Map<Long, ClaimSlaDto> slaMap = new HashMap<>();
        ClaimSlaDto claimSlaDto = null;

        for (LoggedInUserClaimData claimData : loggedInUserClaimDataList) {
            if (convertClaimStageToItsCorrespondingConstant(claimData.getStage()).equals(ClaimStage.INITIAL_AUTHORIZATION)) {
                initialAuthorizationStageTransactionsOfCurrentClaim = claimAssignmentList.stream().filter(object -> "Initial Authorization".equals(object.getClaimStageMst().getName()) && claimData.getClaimID().equals(object.getClaimInfo().getId())).collect(Collectors.toList());
                claimSlaDto = deriveSla(ClaimStage.INITIAL_AUTHORIZATION, initialAuthorizationStageTransactionsOfCurrentClaim);
                slaMap.put(claimData.getClaimID(), claimSlaDto);
            } else if (convertClaimStageToItsCorrespondingConstant(claimData.getStage()).equals(ClaimStage.ENHANCEMENT)) {
                enhancementStageTransactionsOfCurrentClaim = claimAssignmentList.stream().filter(object -> "Enhancement".equals(object.getClaimStageMst().getName()) && claimData.getClaimID().equals(object.getClaimInfo().getId())).collect(Collectors.toList());
                claimSlaDto = deriveSla(ClaimStage.ENHANCEMENT, enhancementStageTransactionsOfCurrentClaim);
                slaMap.put(claimData.getClaimID(), claimSlaDto);
            } else if (convertClaimStageToItsCorrespondingConstant(claimData.getStage()).equals(ClaimStage.DISCHARGE)) {
                dischargeStageTransactionsOfCurrentClaim = claimAssignmentList.stream().filter(object -> "Discharge".equals(object.getClaimStageMst().getName()) && claimData.getClaimID().equals(object.getClaimInfo().getId())).collect(Collectors.toList());
                claimSlaDto = deriveSla(ClaimStage.DISCHARGE, dischargeStageTransactionsOfCurrentClaim);
                slaMap.put(claimData.getClaimID(), claimSlaDto);
            } else {
                finalClaimStageTransactionsOfCurrentClaim = claimAssignmentList.stream().filter(object -> "Final Claim".equals(object.getClaimStageMst().getName()) && claimData.getClaimID().equals(object.getClaimInfo().getId())).collect(Collectors.toList());
                claimSlaDto = deriveSla(ClaimStage.FINAL_CLAIM, finalClaimStageTransactionsOfCurrentClaim);
                slaMap.put(claimData.getClaimID(), claimSlaDto);
            }
        }
        return slaMap;
    }

    private ClaimStage convertClaimStageToItsCorrespondingConstant(String claimStage) {
        if ("Initial Authorization".equals(claimStage))
            return ClaimStage.INITIAL_AUTHORIZATION;
        else if ("Enhancement".equals(claimStage))
            return ClaimStage.ENHANCEMENT;
        else if ("Discharge".equals(claimStage))
            return ClaimStage.DISCHARGE;
        else
            return ClaimStage.FINAL_CLAIM;
    }

    private ClaimSlaDto deriveSla(ClaimStage stage, List<ClaimAssignment> claimAssignmentList) {
        long totalMinutesConsumed = 0;
        long consumedTime = 0;
        for (ClaimAssignment claimAssignment : claimAssignmentList) {
            if (claimAssignment.getCompletedDateTime() != null)
                consumedTime = ChronoUnit.MINUTES.between(claimAssignment.getAssignedDateTime(), claimAssignment.getCompletedDateTime());
            else
                consumedTime = ChronoUnit.MINUTES.between(claimAssignment.getAssignedDateTime(), LocalDateTime.now());
            totalMinutesConsumed = totalMinutesConsumed + consumedTime;
        }
        double slaPercentage = getSlaPercentage(stage, totalMinutesConsumed);
        return new ClaimSlaDto(stage.name(), slaPercentage);
    }

    private Double getSlaPercentage(ClaimStage stage, Long totalMinutesConsumed) {
        Map<ClaimStage, Long> stageWiseSlaMap = masterStore.getStageWiseSlaMap();
        /*long initialAuthorizationSla = 15L;
        long enhancementSla = 15L;
        long dischargeSla = 60L;
        long finalClaimSla = 60L;*/
        double slaPercentage = 0;
        if (ClaimStage.INITIAL_AUTHORIZATION.equals(stage))
            slaPercentage = (totalMinutesConsumed / stageWiseSlaMap.get(ClaimStage.INITIAL_AUTHORIZATION)) * 100;
        else if (ClaimStage.ENHANCEMENT.equals(stage))
            slaPercentage = (totalMinutesConsumed / stageWiseSlaMap.get(ClaimStage.ENHANCEMENT)) * 100;
        else if (ClaimStage.DISCHARGE.equals(stage))
            slaPercentage = (totalMinutesConsumed / stageWiseSlaMap.get(ClaimStage.DISCHARGE)) * 100;
        else
            slaPercentage = (totalMinutesConsumed / stageWiseSlaMap.get(ClaimStage.FINAL_CLAIM)) * 100;
        return slaPercentage;
    }
}
