package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.repo.StatusMstRepo;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ClaimStatus;
import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;
import com.healspan.claimservice.mstupload.claim.constants.Constant.UserRole;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;







import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.master.UserRoleMst;
import com.healspan.claimservice.mstupload.claim.repo.ClaimAssignmentRepo;
import com.healspan.claimservice.mstupload.repo.*;
import com.healspan.claimservice.mstupload.comparator.UserMstComparator;

@Component
public class ClaimAssignmentUtil {
    @Autowired
    private StatusMstRepo statusMstRepo;

    @Autowired
    private UserMstRepo userMstRepo;

    @Autowired
    private UserRoleMstRepo userRoleMstRepo;

    @Autowired
    private ClaimAssignmentRepo claimAssignmentRepo;

    private Queue<UserMst> healspanUserQueue = new LinkedList<>();

    private Logger logger = LoggerFactory.getLogger(ClaimAssignmentUtil.class);

    public StatusMst getNextStatus(AssignmentFlowType assignmentFlowType, long claimStageId, UserMst user) {
        logger.debug("ClaimAssignmentUtil::getNextStatus --Start");
        StatusMst statusMst = null;
        try {
            String userRoleName = user.getUserRoleMst().getName();
            logger.info("ClaimAssignmentUtil::getNextStatus::AssignmentFlowType::{}::ClaimStageId::{}::userRole::{}", assignmentFlowType, claimStageId, userRoleName);
            statusMst = getStatusAsPerAssignmentFlowType(assignmentFlowType, userRoleName, claimStageId);
        }catch (Exception e){
            logger.error("ClaimAssignmentUtil::getNextStatus::Exception::{}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimAssignmentUtil::getNextStatus --End");
        return statusMst;
    }

    private StatusMst getStatusAsPerAssignmentFlowType(AssignmentFlowType assignmentFlowType,String userRole,long stageId){
        logger.debug("ClaimAssignmentUtil::getStatusAsPerAssignmentFlowType --Start");
        StatusMst statusToBeReturned = null;
        List<StatusMst> allStatusesOfCurrentStage = statusMstRepo.findAll().stream().filter(obj->obj.getClaimStage().getId()==stageId).collect(Collectors.toList());
        if(AssignmentFlowType.HEALSPAN_USER_APPROVED_CLAIM.equals(assignmentFlowType) || AssignmentFlowType.HOSPITAL_USER_SUBMITTED_CLAIM.equals(assignmentFlowType)){
            if(UserRole.HEALSPAN.name().equalsIgnoreCase(userRole)){
                statusToBeReturned = getStatus(ClaimStatus.PENDING_HS_APPROVAL,allStatusesOfCurrentStage);
            }else if(UserRole.TPA.name().equalsIgnoreCase(userRole)){
                statusToBeReturned = getStatus(ClaimStatus.PENDING_TPA_APPROVAL,allStatusesOfCurrentStage);
            }
        }else if(AssignmentFlowType.HEALSPAN_USER_QUERIED_AGAINST_CLAIM.equals(assignmentFlowType) || AssignmentFlowType.TPA_USER_QUERIED_AGAINST_CLAIM.equals(assignmentFlowType)){
            if(UserRole.HOSPITAL.name().equalsIgnoreCase(userRole)){
                statusToBeReturned = getStatus(ClaimStatus.PENDING_DOCUMENTS,allStatusesOfCurrentStage);
            }else if(UserRole.HEALSPAN.name().equalsIgnoreCase(userRole)){
                statusToBeReturned = getStatus(ClaimStatus.TPA_QUERY,allStatusesOfCurrentStage);
            }
        }
        logger.debug("ClaimAssignmentUtil::getStatusAsPerAssignmentFlowType --End");
        return statusToBeReturned;
    }

    private StatusMst getStatus(ClaimStatus claimStatus,List<StatusMst> allStatusesOfCurrentStage){
        logger.debug("ClaimAssignmentUtil::getStatus --Start");
        for(StatusMst status : allStatusesOfCurrentStage){
            if(claimStatus.name().equalsIgnoreCase(status.getName().replaceAll("\\s+", "_"))) {
                logger.info("ClaimAssignmentUtil::getStatus::Returned State Name::{}::Returned State ID::{}",status.getName(),status.getId());
                return status;
            }
        }
        logger.debug("ClaimAssignmentUtil::getStatus --End");
        return null;
    }

    public UserMst getNextUserToBeAssigned(AssignmentFlowType assignmentFlowType,long claimInfoId,long claimStageId){
        logger.debug("ClaimAssignmentUtil::getNextUserToBeAssigned --Start");
        UserMst userToBeAssigned = null;
        try {
            List<ClaimAssignment> recordsOfThisClaimAndItsCurrentStage = claimAssignmentRepo.findAllByClaimInfoIdAndClaimStageMstIdOrderByAssignedDateTimeDesc(claimInfoId, claimStageId);
            List<Long> allUsersWhoWorkedOnThisClaim = recordsOfThisClaimAndItsCurrentStage.stream().map(obj -> obj.getUserMst().getId()).collect(Collectors.toList());
            UserMst lastAssignedUser = recordsOfThisClaimAndItsCurrentStage.get(0).getUserMst();
            long lastAssignedUserId = lastAssignedUser.getId();
            String lastAssignedUserRole = lastAssignedUser.getUserRoleMst().getName();
            logger.info("ClaimAssignmentUtil::getNextUserToBeAssigned::ClaimId::{}::StageId::{}::LastAssignedUser::{}::UserRole::{}::UserId::{}",
                    claimInfoId,claimStageId,lastAssignedUser.getFirstName(),lastAssignedUserRole,lastAssignedUserId);
            String loggerPattern = "ClaimAssignmentUtil::getNextUserToBeAssigned::ClaimId::{}::StageId::{}::ReturnedUserName::{}::UserRole::{}::UserId::{}";
            switch (assignmentFlowType) {
                case HOSPITAL_USER_SUBMITTED_CLAIM:
                    if (checkIfItsVeryFirstSubmissionOrApproval(allUsersWhoWorkedOnThisClaim, lastAssignedUserId)) {
                        logger.info("ClaimAssignmentUtil::getNextUserToBeAssigned::ClaimId::{}::StageId::{}::Its a Very First Submission/Approval....!!!!", claimInfoId, claimStageId);
                        if (UserRole.HOSPITAL.name().equalsIgnoreCase(lastAssignedUserRole)) {
                            userToBeAssigned = getUserByRoundRobin(UserRole.HEALSPAN);
                            logger.info(loggerPattern, claimInfoId, claimStageId, userToBeAssigned.getFirstName(), userToBeAssigned.getUserRoleMst().getName(), userToBeAssigned.getId());
                        } else if (UserRole.HEALSPAN.name().equalsIgnoreCase(lastAssignedUserRole)) {
                            UserRoleMst userRole = userRoleMstRepo.findByName(UserRole.TPA.name());
                            userToBeAssigned = userMstRepo.findAllByUserRoleMst(userRole).get(0);
                            logger.info(loggerPattern, claimInfoId, claimStageId, userToBeAssigned.getFirstName(), userToBeAssigned.getUserRoleMst().getName(), userToBeAssigned.getId());
                        }
                    } else {
                        userToBeAssigned = recordsOfThisClaimAndItsCurrentStage.get(1).getUserMst();
                        logger.info(loggerPattern, claimInfoId, claimStageId, userToBeAssigned.getFirstName(), userToBeAssigned.getUserRoleMst().getName(), userToBeAssigned.getId());
                    }
                case HEALSPAN_USER_APPROVED_CLAIM :
                    if (UserRole.HEALSPAN.name().equalsIgnoreCase(lastAssignedUserRole)) {
                        UserRoleMst userRole = userRoleMstRepo.findByName(UserRole.TPA.name());
                        userToBeAssigned = userMstRepo.findAllByUserRoleMst(userRole).get(0);
                        logger.info(loggerPattern, claimInfoId, claimStageId, userToBeAssigned.getFirstName(), userToBeAssigned.getUserRoleMst().getName(), userToBeAssigned.getId());
                    }
            }
        }catch (Exception e){
            logger.error("ClaimAssignmentUtil::getNextUserToBeAssigned::Exception::{}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimAssignmentUtil::getNextUserToBeAssigned --End");
        return userToBeAssigned;
    }

    private boolean checkIfItsVeryFirstSubmissionOrApproval(List<Long> userIdList, long userId){
        logger.debug("ClaimAssignmentUtil::checkIfItsVeryFirstSubmissionOrApproval --Start");
        int frequency = Collections.frequency(userIdList,userId);
        logger.debug("ClaimAssignmentUtil::checkIfItsVeryFirstSubmissionOrApproval --End");
        return frequency==1;
    }

    public UserMst getUserByRoundRobin(UserRole assignmentRole) {
        UserMst assignedUser = null;
        if (assignmentRole.name().equalsIgnoreCase("Healspan")) {
            if (healspanUserQueue.isEmpty()) {
                reloadUserQueue("Healspan");
            }
            assignedUser = peekAndRemoveUserFromQueue("Healspan");
        }
        return assignedUser;
    }

    private void reloadUserQueue(String assignmentRole) {
        LinkedList<UserMst> userMstList = new LinkedList<>();
        UserRoleMst healspanUserRole = userRoleMstRepo.findByName(assignmentRole);
        userMstList.addAll(userMstRepo.findAllByUserRoleMst(healspanUserRole));
        userMstList.sort(new UserMstComparator());
        healspanUserQueue = userMstList;
        logger.info("User Queue Reloaded...!!!");
    }

    private UserMst peekAndRemoveUserFromQueue(String assignmentRole) {
        UserMst lastAssignedHealspanUser = getLastAssignedHealspanUser();
        logger.info("Print Last Assigned Healspan User :: {}", lastAssignedHealspanUser);
        logger.info("Print User Queue :: {}", healspanUserQueue);
        Optional<UserMst> lastUserOfQueue = healspanUserQueue.stream().reduce((first, second) -> second);
        logger.info("Print Last User Of The Queue :: {}", lastAssignedHealspanUser);
        UserMst peekedUser = healspanUserQueue.peek();
        if (peekedUser.getId() > lastAssignedHealspanUser.getId()) {
            healspanUserQueue.remove(peekedUser);
            logger.info("PeekedUserId is greater than lastAssignedHealspanUserId...!!!");
        } else if(lastUserOfQueue.isPresent() && lastUserOfQueue.get().getId().equals(lastAssignedHealspanUser.getId())){
            reloadUserQueue(assignmentRole);
            peekedUser = healspanUserQueue.peek();
            healspanUserQueue.remove(peekedUser);
            logger.info("PeekedUserId matched with lastAssignedHealspanUserId...!!!");
        }else {
            long iteratedUsedId = 0;
            while (iteratedUsedId <= lastAssignedHealspanUser.getId()) {
                peekedUser = healspanUserQueue.peek();
                healspanUserQueue.remove(peekedUser);
                iteratedUsedId = peekedUser.getId();
            }
            logger.info("PeekedUserId is lesser than lastAssignedHealspanUserId...!!!");
        }
        logger.info("Returned Healspan User For Assignment :: {}", peekedUser);
        return peekedUser;
    }

    private UserMst getLastAssignedHealspanUser() {
        List<UserMst> allUserList = userMstRepo.findAll();
        List<UserMst> healspanUserList = allUserList.stream().filter(obj -> "Healspan".equalsIgnoreCase(obj.getUserRoleMst().getName())).collect(Collectors.toList());
        List<Long> healspanUserIdList = healspanUserList.stream().map(obj -> obj.getId()).collect(Collectors.toList());
        ClaimAssignment lastAssignmentRecord = claimAssignmentRepo.findFirst1ByUserMstIdInOrderByAssignedDateTimeDesc(healspanUserIdList);
        return lastAssignmentRecord.getUserMst();
    }
}