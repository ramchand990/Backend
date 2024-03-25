package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.business.InsuranceInfo;
import com.healspan.claimservice.mstupload.claim.dto.business.InsuranceInfoDto;
import com.healspan.claimservice.mstupload.claim.repo.InsuranceInfoRepo;
import com.healspan.claimservice.mstupload.repo.InsuranceCompanyMstRepo;
import com.healspan.claimservice.mstupload.repo.RelationshipMstRepo;
import com.healspan.claimservice.mstupload.repo.TpaMstRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InsuranceInfoUtil {

    private Logger logger = LoggerFactory.getLogger(InsuranceInfoUtil.class);

    @Autowired
    private InsuranceInfoRepo insuranceInfoRepo;
    @Autowired
    private InsuranceCompanyMstRepo insuranceCompanyMstRepo;
    @Autowired
    private TpaMstRepo tpaMstRepo;
    @Autowired
    private RelationshipMstRepo relationshipMstRepo;
    @Autowired
    private ModelMapper modelMapper;

    public InsuranceInfo saveOrUpdateInsuranceInfo(InsuranceInfoDto dto){
        logger.debug("InsuranceInfoUtil::saveOrUpdateInsuranceInfo requestedData: {} --Start",dto);
        InsuranceInfo insuranceInfoDao = modelMapper.map(dto, InsuranceInfo.class);
        if(dto.getId()!=null && dto.getClaimStageLinkId()!=null) {
            insuranceInfoDao.setId(dto.getId());
        }
        logger.debug("Find insuranceCompany record from db for insuranceCompanyId:{} and set to InsuranceInfo",dto.getInsuranceCompanyId());
        insuranceInfoDao.setInsuranceCompanyMst(insuranceCompanyMstRepo.findById(dto.getInsuranceCompanyId()).get());
        logger.debug("Find TPA record from db for tpaId:{} and set to InsuranceInfo",dto.getTpaId());
        insuranceInfoDao.setTpaMst(tpaMstRepo.findById(dto.getTpaId()).get());
        logger.debug("Find Relationship record from db for relationShipId:{} and set to InsuranceInfo",dto.getRelationshipId());
        insuranceInfoDao.setRelationshipMst(relationshipMstRepo.findById(dto.getRelationshipId()).get());
        logger.debug("Save updated InsuranceInfo record in db");
        insuranceInfoRepo.save(insuranceInfoDao);
        logger.debug("InsuranceInfoUtil::saveOrUpdateInsuranceInfo responseData: {} --End",insuranceInfoDao);
        return insuranceInfoDao;
    }

    public ClaimStageLink copyExistingInsuranceInfo(ClaimStageLink existingClaimStageLinkDao, ClaimStageLink newClaimStageLinkDao) {
        logger.debug("InsuranceInfoUtil::copyExistingInsuranceInfoToNewInsuranceInfo --Start");
        InsuranceInfo existingInsuranceInfo = existingClaimStageLinkDao.getInsuranceInfo();
        InsuranceInfoDto insuranceInfoDto = modelMapper.map(existingInsuranceInfo, InsuranceInfoDto.class);
       /* insuranceInfoDto.setId(null);*/
        InsuranceInfo InsuranceInfo = modelMapper.map(insuranceInfoDto, InsuranceInfo.class);
        logger.debug("Save Insurance record in db");
        insuranceInfoRepo.save(InsuranceInfo);

        newClaimStageLinkDao.setInsuranceInfo(InsuranceInfo);
        logger.debug("InsuranceInfoUtil::copyExistingInsuranceInfoToNewInsuranceInfo --End");
        return newClaimStageLinkDao;
    }
}
