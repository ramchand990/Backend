package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.constants.Constant;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimInfo;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.business.Document;
import com.healspan.claimservice.mstupload.claim.dao.master.*;
import com.healspan.claimservice.mstupload.claim.dto.business.*;
import com.healspan.claimservice.mstupload.claim.repo.ClaimAssignmentRepo;
import com.healspan.claimservice.mstupload.claim.repo.ClaimInfoRepo;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import com.healspan.claimservice.mstupload.claim.repo.DocumentRepo;
import com.healspan.claimservice.mstupload.claim.util.ClaimAssignmentUtil;
import com.healspan.claimservice.mstupload.repo.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.healspan.claimservice.mstupload.comparator.UserMstComparator;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Component
public class ClaimAssignmentServiceImpl implements ClaimAssignmentService {

    private final Logger logger = LoggerFactory.getLogger(ClaimAssignmentServiceImpl.class);

    @Autowired
    private ClaimAssignmentRepo claimAssignmentRepo;

    @Autowired
    private UserMstRepo userMstRepo;

    @Autowired
    private UserRoleMstRepo userRoleMstRepo;

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private StatusMstRepo statusMstRepo;

    @Autowired
    private DocumentTypeMstRepo documentTypeMstRepo;

    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MandatoryDocumentsMstRepo mandatoryDocumentsMstRepo;
    @Autowired
    private ClaimInfoRepo claimInfoRepo;
    @Autowired
    private ClaimStageMstRepo claimStageMstRepo;

    @Autowired
    private ClaimAssignmentUtil claimAssignmentUtil;
    @Autowired
    private UpdateClaimAssignmentService updateClaimAssignmentService;

    private Queue<UserMst> healspanUsers = new LinkedList<>();

    public ClaimAssignment assignClaim(ClaimStageLink claimStageLink, UpdatedDetailsForClaimAssignment detailsForClaimAssignment) {
        logger.debug("ClaimAssignmentServiceImpl::assignClaim requestedData:{}",claimStageLink+"and"+detailsForClaimAssignment);
        ClaimInfo claimInfo = claimStageLink.getClaimInfo();
        ClaimStageMst claimStage = claimStageLink.getClaimStageMst();
        logger.debug("Update Closed Data time records in db for ClaimAssignment");
        claimAssignmentRepo.updateClosedDateTime(claimInfo.getId(), claimStage.getId());
        ClaimAssignment claimAssignment = new ClaimAssignment();
        claimAssignment.setUserMst(detailsForClaimAssignment.getUserToBeAssigned());
        claimAssignment.setStatusMst(detailsForClaimAssignment.getStatusToBeAssigned());
        claimAssignment.setClaimStageLink(claimStageLink);
        claimAssignment.setAssignedDateTime(LocalDateTime.now());
        claimAssignment.setClaimInfo(claimStageLink.getClaimInfo());
        claimAssignment.setClaimStageMst(claimStageLink.getClaimStageMst());
        logger.debug("Save Claim Assignment Data in db");
        claimAssignmentRepo.save(claimAssignment);

        logger.info("New assignment created successfully for ClaimId: {}, and assigned to: {}"
                , claimStageLink.getClaimInfo().getId(), claimAssignment.getUserMst().getId());
        return claimAssignment;
    }

    private void updateCloseDateTime(ClaimStageLink claimStageLink) {
        ClaimStageLinkDto claimStageLinkDto = modelMapper.map(claimStageLink, ClaimStageLinkDto.class);
        long id = claimStageLinkDto.getClaimInfo().getId();
        long claimStageMstID = claimStageLinkDto.getClaimStageMst().getClaimStageMstId();
        claimAssignmentRepo.updateClosedDateTime(id, claimStageMstID);
    }

    public UserMst getAssignee(ClaimStageLink claimStageLink, String assignmentRole) {
        UserMst assignedUser = null;
        //check for earlier assignment to HS user, if found then assign claim to that user
        if (assignmentRole.equalsIgnoreCase("Healspan")) {
            //get all links for claim, find HS assignment, if found reassign it to same user
            List<ClaimStageLink> claimStageLinks = claimStageLinkRepo.findAllByClaimInfoId(claimStageLink.getClaimInfo().getId());

            for (ClaimStageLink link : claimStageLinks) {
                List<ClaimAssignment> claimAssignments = claimAssignmentRepo.findAllByClaimStageLinkId(link.getId());
                for (ClaimAssignment assignment : claimAssignments) {
                    assignedUser = assignment.getUserMst();
                    if (assignedUser.getUserRoleMst().getName().equalsIgnoreCase(assignmentRole))
                        return assignedUser;
                }
            }

            // First time Assignment for claim, get HS user in Round robin method
            if (healspanUsers.isEmpty()) {
                reloadUserQueue(assignmentRole);
            }
            assignedUser = peekAndRemoveUserFromQueue(assignmentRole);
        }
        if (assignmentRole.equalsIgnoreCase("RPA")) {
            UserRoleMst userRole = userRoleMstRepo.findByName(assignmentRole);
            assignedUser = userMstRepo.findAllByUserRoleMst(userRole).get(0);
        }
        if (assignmentRole.equalsIgnoreCase("Hospital")) {
            List<ClaimStageLink> claimStageLinks = claimStageLinkRepo.findAllByClaimInfoId(claimStageLink.getClaimInfo().getId());
            assignedUser = claimStageLinks.get(0).getUserMst();
        }
        return assignedUser;
    }

    public void reloadUserQueue(String assignmentRole) {
        LinkedList<UserMst> userMstList = new LinkedList<>();
        UserRoleMst healspanUserRole = userRoleMstRepo.findByName(assignmentRole);
        userMstList.addAll(userMstRepo.findAllByUserRoleMst(healspanUserRole));
        userMstList.sort(new UserMstComparator());
        healspanUsers = userMstList;
        logger.info("User Queue Reloaded...!!!");
    }

    public UserMst peekAndRemoveUserFromQueue(String assignmentRole) {
        UserMst lastAssignedHealspanUser = getLastAssignedHealspanUser();
        logger.info("Print Last Assigned Healspan User :: {}", lastAssignedHealspanUser);
        logger.info("Print User Queue :: {}", healspanUsers);
        UserMst lastUserOfQueue = healspanUsers.stream().reduce((first, second) -> second).get();
        logger.info("Print Last User Of The Queue :: {}", lastAssignedHealspanUser);
        UserMst peekedUser = healspanUsers.peek();
        if (peekedUser.getId() > lastAssignedHealspanUser.getId()) {
            healspanUsers.remove(peekedUser);
            logger.info("PeekedUserId is greater than lastAssignedHealspanUserId...!!!");
        } else if (lastUserOfQueue.getId() == lastAssignedHealspanUser.getId()) {
            reloadUserQueue(assignmentRole);
            peekedUser = healspanUsers.peek();
            healspanUsers.remove(peekedUser);
            logger.info("PeekedUserId matched with lastAssignedHealspanUserId...!!!");
        } else {
            long iteratedUsedId = 0;
            while (iteratedUsedId <= lastAssignedHealspanUser.getId()) {
                peekedUser = healspanUsers.peek();
                healspanUsers.remove(peekedUser);
                iteratedUsedId = peekedUser.getId();
            }
            logger.info("PeekedUserId is lesser than lastAssignedHealspanUserId...!!!");
        }
        logger.info("Returned Healspan User For Assignment :: {}", peekedUser);
        return peekedUser;
    }

    public UserMst getLastAssignedHealspanUser() {
        List<UserMst> allUserList = userMstRepo.findAll();
        List<UserMst> healspanUserList = allUserList.stream().filter(obj -> "Healspan".equalsIgnoreCase(obj.getUserRoleMst().getName())).collect(Collectors.toList());
        List<Long> healspanUserIdList = healspanUserList.stream().map(obj -> obj.getId()).collect(Collectors.toList());
        ClaimAssignment lastAssignmentRecord = claimAssignmentRepo.findFirst1ByUserMstIdInOrderByAssignedDateTimeDesc(healspanUserIdList);
        UserMst lastAssignedHealspanUser = lastAssignmentRecord.getUserMst();
        return lastAssignedHealspanUser;
    }

    @Override
    public ResponseClaimStatusDto assignedCommentProcess(CommentDto dto) {
        logger.info("ClaimAssignmentServiceImpl::assignedCommentProcess --Start");
        ResponseClaimStatusDto response = new ResponseClaimStatusDto();
        response.setClaimId(dto.getClaimId());
        try {
            AssignmentDetail details = updateClaimAssignmentService.prepareAssignmentDetail(modelMapper.map(dto,BaseRequestDto.class));
            updateClaimAssignmentService.setNewUserAndStatus(details);
            updateClaimAssignmentService.insertCommunicationDetailsInCaseAssignment(details);
            List<Long> documentIdList = dto.getDocumentIds();
            for (Long pendingDoc : documentIdList) {
                Optional<Document> documentDao = documentRepo.findByClaimStageLinkId(pendingDoc, details.getClaimStageLink().getId());
                if (documentDao.isPresent()) {
                    documentDao.get().setStatus(false);
                    documentDao.get().setClaimStageLink(details.getClaimStageLink());
                    documentDao.get().setDocumentName(null);
                    documentDao.get().setDocumentPath(null);
                    logger.debug("Updating document = " + documentDao.get());
                    documentRepo.save(documentDao.get());
                } else {
                    Document document = new Document();
                    long pendingDocId = pendingDoc;
                    Optional<MandatoryDocumentsMst> mandatoryDocumentsMst = mandatoryDocumentsMstRepo.findById(pendingDocId);
                    document.setClaimStageLink(details.getClaimStageLink());
                    document.setMandatoryDocumentsMst(mandatoryDocumentsMst.get());
                    logger.debug("Creating document = " + document);
                    documentRepo.save(document);
                }
            }
        } catch (Exception e) {
            logger.error("ClaimAssignmentServiceImpl::assignedCommentProcess::Error::{}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(String.valueOf(Constant.ResponseStatus.FAILED));
            return response;
        }
        logger.info("ClaimAssignmentServiceImpl::assignedCommentProcess --End");
        response.setResponseStatus(String.valueOf(Constant.ResponseStatus.SUCCESS));
        return response;
    }
}