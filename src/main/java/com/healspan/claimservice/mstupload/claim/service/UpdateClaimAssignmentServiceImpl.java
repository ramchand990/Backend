package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.constants.Constant;
import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ClaimStatus;
import com.healspan.claimservice.mstupload.claim.constants.Constant.UserRole;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserRoleMst;
import com.healspan.claimservice.mstupload.claim.dto.business.AssignmentDetail;
import com.healspan.claimservice.mstupload.claim.dto.business.BaseRequestDto;
import com.healspan.claimservice.mstupload.claim.repo.ClaimAssignmentRepo;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import com.healspan.claimservice.mstupload.claim.util.UserQueueUtil;
import com.healspan.claimservice.mstupload.repo.StatusMstRepo;
import com.healspan.claimservice.mstupload.repo.UserMstRepo;
import com.healspan.claimservice.mstupload.repo.UserRoleMstRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UpdateClaimAssignmentServiceImpl implements UpdateClaimAssignmentService {

    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;
    @Autowired
    private ClaimAssignmentRepo claimAssignmentRepo;
    @Autowired
    private UserRoleMstRepo userRoleMstRepo;
    @Autowired
    private UserMstRepo userMstRepo;
    @Autowired
    private StatusMstRepo statusMstRepo;
    @Autowired
    private UserQueueUtil userQueueUtil;

    private Logger logger = LoggerFactory.getLogger(UpdateClaimAssignmentServiceImpl.class);

    @Override
    public AssignmentDetail prepareAssignmentDetail(BaseRequestDto dto) {
        logger.debug("UpdateClaimAssignmentServiceImpl::prepareAssignmentDetail requestedData: {} --START",dto);
        AssignmentDetail details = new AssignmentDetail();
        ClaimStageLink claimStageLink = null;
        Optional<ClaimStageLink> claimStageLinkOptional =  claimStageLinkRepo.findByClaimInfoIdAndClaimStageMstId(dto.getClaimId(),dto.getStageId());
        if(claimStageLinkOptional.isPresent()) {
            claimStageLink = claimStageLinkOptional.get();
            details.setClaimStageLink(claimStageLink);
            details.setClaimInfo(claimStageLink.getClaimInfo());
            details.setClaimStage(claimStageLink.getClaimStageMst());
            details.setCurrentStatus(claimStageLink.getStatusMst());
        }
        List<ClaimAssignment> claimAssignmentsOfCurrentStageAndIterationInstance = claimAssignmentRepo.findAllByClaimInfoIdOrderByAssignedDateTimeDesc(dto.getClaimId());
        details.setIterationInstance(claimAssignmentsOfCurrentStageAndIterationInstance.get(0).getIterationInstance());
        if(claimAssignmentsOfCurrentStageAndIterationInstance.size()>1)
            details.setLastOwner(claimAssignmentsOfCurrentStageAndIterationInstance.get(1).getUserMst());
        details.setFlowType(dto.getFlowType());
        details.setComments(dto.getTransferComment());
        details.setClaimAssignmentsList(claimAssignmentsOfCurrentStageAndIterationInstance);
        details.setUsersWorkOnClaim(getAllUsersWorkedOnTheClaim(claimAssignmentsOfCurrentStageAndIterationInstance));
        getNextUserAndNextStatus(details);
        logger.debug("UpdateClaimAssignmentServiceImpl::prepareAssignmentDetail responseData: {} --END",details);
        return details;
    }

    private Map<String,UserMst> getAllUsersWorkedOnTheClaim(List<ClaimAssignment> claimAssignmentList){
        logger.debug("UpdateClaimAssignmentServiceImpl::getAllUsersWorkedOnTheClaim requestedClaimAssignmentList: {} --Start",claimAssignmentList);
        Map<String,UserMst> userMap = new HashMap<>();
        for(ClaimAssignment ca : claimAssignmentList){
            UserMst user = ca.getUserMst();
            if(UserRole.HEALSPAN.name().equalsIgnoreCase(user.getUserRoleMst().getName()))
                userMap.put(UserRole.HEALSPAN.name(),user);
            else if(UserRole.HOSPITAL.name().equalsIgnoreCase(user.getUserRoleMst().getName()))
                userMap.put(UserRole.HOSPITAL.name(),user);
            else if(UserRole.TPA.name().equalsIgnoreCase(user.getUserRoleMst().getName()))
                userMap.put(UserRole.TPA.name(),user);
        }
        logger.debug("UpdateClaimAssignmentServiceImpl::getAllUsersWorkedOnTheClaim responseData: {} --End",userMap);
        return userMap;
    }

    public AssignmentDetail getNextUserAndNextStatus(AssignmentDetail details) {
        logger.debug("UpdateClaimAssignmentServiceImpl::getNextUserAndNextStatus requestedAssignmentDetail: {}  --START",details);
        Map<String,UserMst> userMap = details.getUsersWorkOnClaim();
        UserMst userToBeAssigned = null;
        List<ClaimAssignment> claimAssignmentsOfCurrentStageAndIterationInstance = details.getClaimAssignmentsList()
                .stream().filter(obj -> obj.getIterationInstance() == details.getIterationInstance()).collect(Collectors.toList());
        List<String> allUserRolesWhoWorkedOnThisClaim = claimAssignmentsOfCurrentStageAndIterationInstance.stream().map(obj -> obj.getUserMst().getUserRoleMst().getName().toUpperCase()).collect(Collectors.toList());
        switch (details.getFlowType()) {
            case HOSPITAL_USER_SUBMITTED_CLAIM:
                if (checkIfItsVeryFirstSubmissionOrApproval(details.getFlowType(), allUserRolesWhoWorkedOnThisClaim)) {
                    userToBeAssigned = userQueueUtil.getUserByRoundRobin(UserRole.HEALSPAN);
                } else if (checkIfClaimIsNotAtInitialStage(details.getClaimStage())) {
                    userToBeAssigned = userMap.get(UserRole.HEALSPAN.name());
                    details.setIterationInstance(details.getIterationInstance() + 1);
                } else {
                    userToBeAssigned = details.getLastOwner();
                }
                break;
            case HEALSPAN_USER_APPROVED_CLAIM:
                if (checkIfItsVeryFirstSubmissionOrApproval(details.getFlowType(), allUserRolesWhoWorkedOnThisClaim)) {
                    UserRoleMst userRole = userRoleMstRepo.findByName(UserRole.TPA.name());
                    userToBeAssigned = userMstRepo.findAllByUserRoleMst(userRole).get(0);
                } else if (checkIfClaimIsNotAtInitialStage(details.getClaimStage())) {
                    userToBeAssigned = userMap.get(UserRole.TPA.name());
                } else if (checkIfLastOwnerWasNotTpa(details.getLastOwner().getUserRoleMst())) {
                    userToBeAssigned = findTpaUserInTheAssignments(claimAssignmentsOfCurrentStageAndIterationInstance);
                } else {
                    userToBeAssigned = details.getLastOwner();
                }
                break;
            case HEALSPAN_USER_QUERIED_AGAINST_CLAIM:
                if (checkIfCurrentStatusIsTpaQuery(details.getCurrentStatus()))
                    userToBeAssigned = details.getClaimInfo().getUserMst();
                else
                    userToBeAssigned = details.getLastOwner();
                break;
            case TPA_USER_QUERIED_AGAINST_CLAIM:
                userToBeAssigned = details.getLastOwner();
                break;
            case TPA_USER_APPROVED_CLAIM:
            case TPA_USER_REJECTED_CLAIM:
                userToBeAssigned = details.getClaimInfo().getUserMst();
                break;
        }
        details.setNextUserToBeAssigned(userToBeAssigned);
        details.setNextStatusToBeAssigned(getNextStatusToBeAssigned(details.getFlowType(), details.getClaimStage().getId()));
        logger.debug("UpdateClaimAssignmentServiceImpl::getNextUserAndNextStatus responseData: {} --END",details);
        return details;
    }

    private boolean checkIfItsVeryFirstSubmissionOrApproval(AssignmentFlowType flowType,List<String> userRoleList){
        if(AssignmentFlowType.HOSPITAL_USER_SUBMITTED_CLAIM.equals(flowType))
            return !userRoleList.contains(UserRole.HEALSPAN.name());
        else
            return !userRoleList.contains(UserRole.TPA.name());
    }

    private boolean checkIfCurrentStatusIsTpaQuery(StatusMst status){
        String statusName = status.getName().replaceAll("\\s+", "_");
        return ClaimStatus.TPA_QUERY.name().equalsIgnoreCase(statusName);
    }

    private boolean checkIfLastOwnerWasNotTpa(UserRoleMst userRole){
        return !UserRole.TPA.name().equalsIgnoreCase(userRole.getName());
    }

    private boolean checkIfAlreadyApproved(StatusMst status) {
        return ClaimStatus.APPROVED.name().equalsIgnoreCase(status.getName());
    }

    private boolean checkIfClaimIsNotAtInitialStage(ClaimStageMst stage) {
        String initialStage = Constant.ClaimStage.INITIAL_AUTHORIZATION.name().replaceAll("_", " ");
        return !initialStage.equalsIgnoreCase(stage.getName());
    }

    private UserMst findTpaUserInTheAssignments(List<ClaimAssignment> claimAssignmentList){
        for(ClaimAssignment ca : claimAssignmentList){
            UserMst user = ca.getUserMst();
            if(UserRole.TPA.name().equalsIgnoreCase(user.getUserRoleMst().getName()))
                return user;
        }
        return null;
    }

    private UserMst findHealspanUserInTheAssignments(List<ClaimAssignment> claimAssignmentList){
        for(ClaimAssignment ca : claimAssignmentList){
            UserMst user = ca.getUserMst();
            if(UserRole.HEALSPAN.name().equalsIgnoreCase(user.getUserRoleMst().getName()))
                return user;
        }
        return null;
    }

    private StatusMst getNextStatusToBeAssigned(AssignmentFlowType assignmentFlowType, long claimStageId){
        logger.debug("UpdateClaimAssignmentServiceImpl::getNextStatusToBeAssigned::START");
        StatusMst statusToBeReturned = null;
        List<StatusMst> allStatusesOfCurrentStage = statusMstRepo.findAll().stream().filter(obj->obj.getClaimStage().getId()==claimStageId).collect(Collectors.toList());
        switch (assignmentFlowType) {
            case HOSPITAL_USER_SUBMITTED_CLAIM:
                statusToBeReturned = getStatus(ClaimStatus.PENDING_HS_APPROVAL,allStatusesOfCurrentStage);
                break;
            case HEALSPAN_USER_APPROVED_CLAIM:
                statusToBeReturned = getStatus(ClaimStatus.PENDING_TPA_APPROVAL,allStatusesOfCurrentStage);
                break;
            case TPA_USER_APPROVED_CLAIM:
                statusToBeReturned = getStatus(ClaimStatus.APPROVED,allStatusesOfCurrentStage);
                break;
            case HEALSPAN_USER_QUERIED_AGAINST_CLAIM:
                statusToBeReturned = getStatus(ClaimStatus.PENDING_DOCUMENTS,allStatusesOfCurrentStage);
                break;
            case TPA_USER_QUERIED_AGAINST_CLAIM:
                statusToBeReturned = getStatus(ClaimStatus.TPA_QUERY,allStatusesOfCurrentStage);
                break;
            case TPA_USER_REJECTED_CLAIM:
                statusToBeReturned = getStatus(ClaimStatus.REJECTED,allStatusesOfCurrentStage);
                break;
        }
        logger.debug("UpdateClaimAssignmentServiceImpl::getNextStatusToBeAssigned::END");
        return statusToBeReturned;
    }

    private StatusMst getStatus(ClaimStatus claimStatus, List<StatusMst> allStatusesOfCurrentStage){
        for(StatusMst status : allStatusesOfCurrentStage){
            if(claimStatus.name().equalsIgnoreCase(status.getName().replaceAll("\\s+", "_"))) {
                return status;
            }
        }
        return null;
    }

    @Override
    public void setNewUserAndStatus(AssignmentDetail detail){
        logger.debug("UpdateClaimAssignmentServiceImpl::setNewUserAndStatus::START");
        ClaimStageLink claimStageLink = detail.getClaimStageLink();
        claimStageLink.setUserMst(detail.getNextUserToBeAssigned());
        claimStageLink.setStatusMst(detail.getNextStatusToBeAssigned());
        claimStageLinkRepo.save(claimStageLink);
        logger.debug("UpdateClaimAssignmentServiceImpl::setNewUserAndStatus::END");
    }

    @Override
    public void insertCommunicationDetailsInCaseAssignment(AssignmentDetail detail) {
        logger.debug("UpdateClaimAssignmentServiceImpl::insertCommunicationDetailsInCaseAssignment::START{} ", detail);
        claimAssignmentRepo.updateClosedDateTime(detail.getClaimInfo().getId(), detail.getClaimStage().getId());
        ClaimAssignment claimAssignment = new ClaimAssignment();
        System.out.println(detail.getNextUserToBeAssigned());
        claimAssignment.setUserMst(detail.getNextUserToBeAssigned());
        claimAssignment.setStatusMst(detail.getNextStatusToBeAssigned());
        claimAssignment.setClaimStageLink(detail.getClaimStageLink());
        claimAssignment.setAssignedDateTime(LocalDateTime.now());
        claimAssignment.setClaimInfo(detail.getClaimInfo());
        claimAssignment.setClaimStageMst(detail.getClaimStage());
        claimAssignment.setIterationInstance(detail.getIterationInstance());
        if(AssignmentFlowType.HOSPITAL_USER_SUBMITTED_CLAIM.equals(detail.getFlowType()) || AssignmentFlowType.HEALSPAN_USER_APPROVED_CLAIM.equals(detail.getFlowType()) || AssignmentFlowType.TPA_USER_APPROVED_CLAIM.equals(detail.getFlowType()))
            claimAssignment.setActionTaken(detail.getComments());
        else
            claimAssignment.setAssigneeComments(detail.getComments());
        claimAssignmentRepo.save(claimAssignment);
        logger.debug("UpdateClaimAssignmentServiceImpl::insertCommunicationDetailsInCaseAssignment::END");
    }
}
