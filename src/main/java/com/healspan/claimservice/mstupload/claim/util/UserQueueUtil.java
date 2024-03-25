package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.constants.Constant.UserRole;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserRoleMst;
import com.healspan.claimservice.mstupload.claim.repo.ClaimAssignmentRepo;
import com.healspan.claimservice.mstupload.comparator.UserMstComparator;
import com.healspan.claimservice.mstupload.repo.UserMstRepo;
import com.healspan.claimservice.mstupload.repo.UserRoleMstRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

@Component
public class UserQueueUtil {

    @Autowired
    private UserMstRepo userMstRepo;
    @Autowired
    private UserRoleMstRepo userRoleMstRepo;
    @Autowired
    private ClaimAssignmentRepo claimAssignmentRepo;

    private Queue<UserMst> healspanUserQueue = new LinkedList<>();

    private Logger logger = LoggerFactory.getLogger(UserQueueUtil.class);

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
        UserMst lastAssignedHealspanUser = null;
        List<UserMst> allUserList = userMstRepo.findAll();
        List<UserMst> healspanUserList = allUserList.stream().filter(obj -> "Healspan".equalsIgnoreCase(obj.getUserRoleMst().getName())).collect(Collectors.toList());
        List<Long> healspanUserIdList = healspanUserList.stream().map(obj -> obj.getId()).collect(Collectors.toList());
        ClaimAssignment lastAssignmentRecord = claimAssignmentRepo.findFirst1ByUserMstIdInOrderByAssignedDateTimeDesc(healspanUserIdList);
        if(lastAssignmentRecord!=null){
            lastAssignedHealspanUser = lastAssignmentRecord.getUserMst();
            return lastAssignedHealspanUser;
        }else{
            return healspanUserQueue.peek();
        }
    }
}
