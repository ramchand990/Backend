package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dto.business.ClaimInfoDto;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import com.healspan.claimservice.mstupload.repo.ClaimStageMstRepo;
import com.healspan.claimservice.mstupload.repo.StatusMstRepo;
import com.healspan.claimservice.mstupload.repo.UserMstRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClaimStageLinkUtil {

    @Autowired
    private ClaimStageMstRepo claimStageMstRepo;
    @Autowired
    private StatusMstRepo statusMstRepo;
    @Autowired
    private UserMstRepo userMstRepo;
    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;

    public ClaimStageLink getClaimStageLink(ClaimInfoDto dto){
        ClaimStageLink claimStageLinkDao = null;
        if(dto.getClaimStageLinkId()==null) {
            claimStageLinkDao = new ClaimStageLink();
            claimStageLinkDao.setCreatedDateTime(LocalDateTime.now());
        }else{
            claimStageLinkDao = claimStageLinkRepo.findById(dto.getClaimStageLinkId()).get();
            claimStageLinkDao.setLastUpdatedDateTime(LocalDateTime.now());
        }
        ClaimStageMst claimStageMstDao = claimStageMstRepo.findById(dto.getClaimStageId()).get();
        claimStageLinkDao.setClaimStageMst(claimStageMstDao);
        StatusMst statusMstDao = statusMstRepo.findById(dto.getStatusId()).get();
        claimStageLinkDao.setStatusMst(statusMstDao);
        UserMst userMstDao = userMstRepo.findById(dto.getUserId()).get();
        claimStageLinkDao.setUserMst(userMstDao);
        return claimStageLinkDao;
    }
}
