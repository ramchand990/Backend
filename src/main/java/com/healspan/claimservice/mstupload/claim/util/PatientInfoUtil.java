package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.*;
import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dto.business.ClaimStageLinkDto;
import com.healspan.claimservice.mstupload.claim.dto.business.PatientAndOtherCostLinkDto;
import com.healspan.claimservice.mstupload.claim.dto.business.PatientInfoDto;
import com.healspan.claimservice.mstupload.claim.repo.*;
import com.healspan.claimservice.mstupload.repo.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class PatientInfoUtil {

    private Logger logger = LoggerFactory.getLogger(PatientInfoUtil.class);

    @Autowired
    private PatientInfoRepo patientInfoRepo;
    @Autowired
    private HospitalMstRepo hospitalMstRepo;
    @Autowired
    private OtherCostsMstRepo otherCostsMstRepo;
    @Autowired
    private RoomCategoryMstRepo roomCategoryMstRepo;
    @Autowired
    private GenderMstRepo genderMstRepo;
    @Autowired
    private PatientAndOtherCostLinkRepo patientAndOtherCostLinkRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserMstRepo userMstRepo;
    @Autowired
    private ClaimAssignmentRepo claimAssignmentRepo;
    @Autowired
    private ClaimInfoRepo claimInfoRepo;
    @Autowired
    private ClaimStageMstRepo claimStageMstRepo;

    public PatientInfo saveOrUpdatePatientInfo(PatientInfoDto dto) {
        logger.debug("PatientInfoUtil::saveOrUpdatePatientInfo requestedData: {} --Start",dto);
        PatientInfo patientInfoDao = modelMapper.map(dto, PatientInfo.class);
        if (dto.getId() != null && dto.getClaimStageLinkId() != null) {
            patientInfoDao.setId(dto.getId());
        }
        logger.debug("Find the Hospital record from db for hospitalId:{} and set to claimInfoDao",dto.getHospitalId());
        patientInfoDao.setHospitalMst(hospitalMstRepo.findById(dto.getHospitalId()).get());
        logger.debug("Find the roomCategory record from db for roomCategoryId:{} and set to patientInfoDao",dto.getRoomCategoryId());
        patientInfoDao.setRoomCategoryMst(roomCategoryMstRepo.findById(dto.getRoomCategoryId()).get());
        logger.debug("Find the Gender record from db for genderId:{} and set to patientInfoDao",dto.getGenderId());
        patientInfoDao.setGenderMst(genderMstRepo.findById(dto.getGenderId()).get());
        logger.debug("Save Patient record in db");
        patientInfoRepo.save(patientInfoDao);
        logger.debug("Successfully saved patientInfo record in db");
        deleteOtherCostDetail(patientInfoDao);
        saveOtherCostDetail(dto.getPatientAndOtherCostLink(), patientInfoDao);
        logger.debug("PatientInfoUtil::saveOrUpdatePatientInfo responseData: {} --End",patientInfoDao);
        return patientInfoDao;
    }

    @Transactional
    private void deleteOtherCostDetail(PatientInfo patientInfoDao) {
        logger.debug("PatientInfoUtil::deleteOtherCostDetail requestedData: {} --Start",patientInfoDao);
        try {
            logger.info("Delete Patient and Other Cost record from db for patientId:{}",patientInfoDao.getId());
            patientAndOtherCostLinkRepo.deleteByPatientInfoId(patientInfoDao.getId());
        } catch (Exception e) {
            logger.error("Exception Details: \n {}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("PatientInfoUtil::deleteOtherCostDetail deleted OtherCostDetail: {} --End",patientInfoDao);
    }

    public void saveOtherCostDetail(List<PatientAndOtherCostLinkDto> dto, PatientInfo patientInfoDao) {
        logger.debug("PatientInfoUtil::saveOtherCostDetail requestedDto: {} and patientInfo: {} --Start",dto,patientInfoDao);
        List<PatientAndOtherCostLink> linkDaoList = new ArrayList<>();
        if (dto != null && !dto.isEmpty()) {
            for (PatientAndOtherCostLinkDto detail : dto) {
                PatientAndOtherCostLink link = new PatientAndOtherCostLink();
                link.setAmount(detail.getAmount());
                logger.info("Find Other Cost record for otherCostId:{} and set to patientAndOtherCostLink ", detail.getId());
                link.setOtherCostsMst(otherCostsMstRepo.findById(detail.getId()).get());
                link.setPatientInfo(patientInfoDao);
                linkDaoList.add(link);
            }
            if (!linkDaoList.isEmpty()) {
                logger.info("save all newly created Patient and OtherCost link");
                patientAndOtherCostLinkRepo.saveAll(linkDaoList);
                logger.info("Successfully saved all Patient and OtherCost link ");
            }
            logger.debug("PatientInfoUtil::saveOtherCostDetail responseData: {}  --End",linkDaoList);
        }
    }

    public ClaimStageLink copyExistingPatientInfo(ClaimStageLink existingClaimStageLinkDao, ClaimStageLink newClaimStageLinkDao) {
        logger.debug("PatientInfoUtil::copyExistingPatientInfoToNewPatientInfo requestedExistingClaimStageLinkDao: {} and newClaimStageLinkDao: {} --Start",existingClaimStageLinkDao,newClaimStageLinkDao);
        PatientInfo existingPatientInfo = existingClaimStageLinkDao.getPatientInfo();
        PatientInfoDto patientInfoDto = modelMapper.map(existingPatientInfo, PatientInfoDto.class);
        /*patientInfoDto.setId(null);*/
        PatientInfo patientInfo = modelMapper.map(patientInfoDto, PatientInfo.class);
        patientInfoRepo.save(patientInfo);

        /*if (!existingPatientInfo.getPatientAndOtherCostLink().isEmpty()) {
            List<PatientAndOtherCostLink> newPatientAndOtherCostLinkDaoList = new ArrayList<>();
            for (PatientAndOtherCostLink patientAndOtherCostLinkDao : existingPatientInfo.getPatientAndOtherCostLink()) {
                PatientAndOtherCostLinkDto patientAndOtherCostLinkDto = modelMapper.map(patientAndOtherCostLinkDao, PatientAndOtherCostLinkDto.class);
                patientAndOtherCostLinkDto.setId(null);
                PatientAndOtherCostLink newPatientAndOtherCostLink = modelMapper.map(patientAndOtherCostLinkDto, PatientAndOtherCostLink.class);
                newPatientAndOtherCostLink.setPatientInfo(newPatientInfo);
                newPatientAndOtherCostLinkDaoList.add(newPatientAndOtherCostLink);
            }
            logger.debug("Save all Patient and other cost link records in db");
            patientAndOtherCostLinkRepo.saveAll(newPatientAndOtherCostLinkDaoList);
        }*/
        newClaimStageLinkDao.setPatientInfo(patientInfo);
        logger.debug("PatientInfoUtil::copyExistingPatientInfoToNewPatientInfo responseData: {} --End",newClaimStageLinkDao);
        return newClaimStageLinkDao;
    }

    public void insertInitialRecordInClaimAssignment(Long userId, ClaimStageLink claimStageLink) {
        logger.debug("PatientInfoUtil::insertInitialRecordInClaimAssignment requestedUserId: {} and claimStageLink: {} --Start",userId,claimStageLink);
        ClaimAssignment claimAssignment = new ClaimAssignment();
        ClaimStageLinkDto claimStageLinkDto = modelMapper.map(claimStageLink, ClaimStageLinkDto.class);
        long claimInfoId = claimStageLinkDto.getClaimInfo().getId();
        long claimStageMstId = claimStageLinkDto.getClaimStageMst().getClaimStageMstId();
        logger.debug("Find Claim Info records in db for claimInfoId: {}",claimInfoId);
        Optional<ClaimInfo> claimInfo = claimInfoRepo.findById(claimInfoId);
        logger.debug("Find Claim Stage records in db for claimStageMstId: {}",claimStageMstId);
        Optional<ClaimStageMst> claimStageMst = claimStageMstRepo.findById(claimStageMstId);
        logger.debug("Find User Mst records in db for userId: {}",userId);
        Optional<UserMst> optional = userMstRepo.findById(userId);
        claimAssignment.setUserMst(optional.get());
        claimAssignment.setClaimStageLink(claimStageLink);
        claimAssignment.setAssignedDateTime(LocalDateTime.now());
        claimAssignment.setClaimInfo(claimInfo.get());
        claimAssignment.setClaimStageMst(claimStageMst.get());
        claimAssignment.setIterationInstance(1l);
        claimAssignment.setStatusMst(claimStageLink.getStatusMst());
        logger.info("Save Claim assignment record in db");
        claimAssignmentRepo.save(claimAssignment);
        logger.info("PatientInfoUtil::insertInitialRecordInClaimAssignment responseData: {} --End",claimAssignment);
    }
}