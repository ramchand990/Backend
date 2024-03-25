package com.healspan.claimservice.mstupload.controller;

import com.healspan.claimservice.mstupload.claim.dao.master.*;
import com.healspan.claimservice.mstupload.model.MasterRequest;
import com.healspan.claimservice.mstupload.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/healspan")
@CrossOrigin
public class MstController {

    private Logger logger = LoggerFactory.getLogger(MstController.class);

    @Autowired
    InsuranceCompanyMstRepo insuranceCompanyMstRepo;
    @Autowired
    ChronicIllnessMstRepo chronicIllnessMstRepo;
    @Autowired
    DiagnosisMstRepo diagnosisMstRepo;
    @Autowired
    GenderMstRepo genderMstRepo;
    @Autowired
    OtherCostsMstRepo otherCostsMstRepo;
    @Autowired
    ProcedureMstRepo procedureMstRepo;
    @Autowired
    RelationshipMstRepo relationshipMstRepo;
    @Autowired
    RoomCategoryMstRepo roomCategoryMstRepo;
    @Autowired
    SpecialityMstRepo specialityMstRepo;
    @Autowired
    TpaMstRepo tpaMstRepo;
    @Autowired
    TreatmentTypeMstRepo treatmentTypeMstRepo;
    @Autowired
    UserMstRepo userMstRepo;
    @Autowired
    UserRoleMstRepo userRoleMstRepo;
    @Autowired
    ClaimStageMstRepo claimStageMstRepo;
    @Autowired
    HospitalMstRepo hospitalMstRepo;
    @Autowired
    StatusMstRepo statusMstRepo;
    @PostMapping("/save/master")
    public void saveInsuranceCompanyMst(@RequestBody MasterRequest req){
        logger.info("Request received to save insuranceCompany for masterName:{}",req.getMstName());
        if("ICM".equalsIgnoreCase(req.getMstName())) {
            List<InsuranceCompanyMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                InsuranceCompanyMst mst = new InsuranceCompanyMst();
                mst.setName(obj);
                logger.info("Add insurance company for insuranceCompanyId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all insurance company");
            logger.info("Save all insurance company to db");
            insuranceCompanyMstRepo.saveAll(daoList);
            logger.info("Successfully saved all insurance company to db");
        }else if("CMR".equalsIgnoreCase(req.getMstName())){
            List<ChronicIllnessMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                ChronicIllnessMst mst = new ChronicIllnessMst();
                mst.setName(obj);
                logger.info("Add chronic illness for chronicIllnessId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all chronic illness");
            logger.info("Save all chronic illness to db");
            chronicIllnessMstRepo.saveAll(daoList);
            logger.info("Successfully saved all chronic illness to db");
        }else if("DMR".equalsIgnoreCase(req.getMstName())){
            List<DiagnosisMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                DiagnosisMst mst = new DiagnosisMst();
                mst.setName(obj);
                logger.info("Add diagnosis for diagnosisId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all diagnosis");
            logger.info("Save all diagnosis to db");
            diagnosisMstRepo.saveAll(daoList);
            logger.info("Successfully saved all diagnosis to db");
        }else if("GMR".equalsIgnoreCase(req.getMstName())){
            List<GenderMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                GenderMst mst = new GenderMst();
                mst.setName(obj);
                logger.info("Add gender for genderId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all gender");
            logger.info("Save all gender to db ");
            genderMstRepo.saveAll(daoList);
            logger.info("Successfully saved all gender to db");
        }else if("OCMR".equalsIgnoreCase(req.getMstName())){
            List<OtherCostsMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                OtherCostsMst mst = new OtherCostsMst();
                mst.setName(obj);
                logger.info("Add other cost for otherCostId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all other cost");
            logger.info("Save all other cost to db ");
            otherCostsMstRepo.saveAll(daoList);
            logger.info("Successfully saved all other cost to db");
        }else if("PMR".equalsIgnoreCase(req.getMstName())){
            List<ProcedureMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                ProcedureMst mst = new ProcedureMst();
                mst.setName(obj);
                logger.info("Add procedure for procedureId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all procedure");
            logger.info("Save all procedure to db ");
            procedureMstRepo.saveAll(daoList);
            logger.info("Successfully saved all procedure to db");
        }else if("RMR".equalsIgnoreCase(req.getMstName())){
            List<RelationshipMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                RelationshipMst mst = new RelationshipMst();
                mst.setName(obj);
                logger.info("Add relationShip for relationShipId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all relationShip");
            logger.info("Save all relationShip to db ");
            relationshipMstRepo.saveAll(daoList);
            logger.info("Successfully saved all relationShip to db");
        }else if("RCMR".equalsIgnoreCase(req.getMstName())){
            List<RoomCategoryMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                RoomCategoryMst mst = new RoomCategoryMst();
                mst.setName(obj);
                logger.info("Add roomCategory for roomCategoryId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all roomCategory");
            logger.info("Save all roomCategory to db ");
            roomCategoryMstRepo.saveAll(daoList);
            logger.info("Successfully saved all roomCategory to db");
        }else if("SMR1".equalsIgnoreCase(req.getMstName())){
            List<SpecialityMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                SpecialityMst mst = new SpecialityMst();
                mst.setName(obj);
                logger.info("Add speciality for specialityId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all speciality");
            logger.info("Save all speciality to db ");
            specialityMstRepo.saveAll(daoList);
            logger.info("Successfully saved all speciality to db");
        }else if("TMR".equalsIgnoreCase(req.getMstName())){
            List<TpaMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                TpaMst mst = new TpaMst();
                mst.setName(obj);
                logger.info("Add Tpa for tpaId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all Tpa");
            logger.info("Save all Tpa to db ");
            tpaMstRepo.saveAll(daoList);
            logger.info("Successfully saved all Tpa to db");
        }else if("TTMR".equalsIgnoreCase(req.getMstName())){
            List<TreatmentTypeMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                TreatmentTypeMst mst = new TreatmentTypeMst();
                mst.setName(obj);
                logger.info("Add treatment type for treatmentType Id:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all treatment type");
            logger.info("Save all treatment type to db ");
            treatmentTypeMstRepo.saveAll(daoList);
            logger.info("Successfully saved all treatment type to db");
        }else if("UMR".equalsIgnoreCase(req.getMstName())){
            List<UserMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                UserMst mst = new UserMst();
                mst.setFirstName(obj);
                logger.info("Add user for userId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all user");
            logger.info("Save all user to db ");
            userMstRepo.saveAll(daoList);
            logger.info("Successfully saved all user to db");
        }else if("URMR".equalsIgnoreCase(req.getMstName())){
            List<UserRoleMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                UserRoleMst mst = new UserRoleMst();
                mst.setName(obj);
                logger.info("Add user role for userRoleId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all user role");
            logger.info("Save all user role to db ");
            userRoleMstRepo.saveAll(daoList);
            logger.info("Successfully saved all user role to db");
        }else if("CSMR".equalsIgnoreCase(req.getMstName())){
            List<ClaimStageMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                ClaimStageMst mst = new ClaimStageMst();
                mst.setName(obj);
                logger.info("Add claim stage for claimStageId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all claim stage");
            logger.info("Save all claim stage to db ");
            claimStageMstRepo.saveAll(daoList);
            logger.info("Successfully saved all claim stage to db");
        }else if("HMR".equalsIgnoreCase(req.getMstName())){
            List<HospitalMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                HospitalMst mst = new HospitalMst();
                mst.setName(obj);
                logger.info("Add hospital for hospitalId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all hospital");
            logger.info("Save all hospital to db ");
            hospitalMstRepo.saveAll(daoList);
            logger.info("Successfully saved all hospital to db");
        }else if("URMR".equalsIgnoreCase(req.getMstName())){
            List<UserRoleMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                UserRoleMst mst = new UserRoleMst();
                mst.setName(obj);
                logger.info("Add user role for userRoleId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all user role");
            logger.info("Save all user role to db ");
            userRoleMstRepo.saveAll(daoList);
            logger.info("Successfully saved all user role to db");
        }else if("UMR".equalsIgnoreCase(req.getMstName())){
            List<UserMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                UserMst mst = new UserMst();
                mst.setFirstName(obj);
                logger.info("Add user for userId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all user");
            logger.info("Save all user to db ");
            userMstRepo.saveAll(daoList);
            logger.info("Successfully saved all user to db");
        }else if("SMR2".equalsIgnoreCase(req.getMstName())){
            List<StatusMst> daoList = new ArrayList<>();
            req.getMstData().stream().forEach(obj -> {
                StatusMst mst = new StatusMst();
                mst.setName(obj);
                logger.info("Add status for statusId:{}",mst.getId());
                daoList.add(mst);
            });
            logger.info("Successfully added all status");
            logger.info("Save all status to db ");
            statusMstRepo.saveAll(daoList);
            logger.info("Successfully saved all status to db");
        }
    }
}
