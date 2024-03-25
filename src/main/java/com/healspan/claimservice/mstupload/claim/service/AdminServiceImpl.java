package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.dao.master.*;
import com.healspan.claimservice.mstupload.claim.dto.master.UserMstDtoNew;
import com.healspan.claimservice.mstupload.claim.model.MasterDetails;
import com.healspan.claimservice.mstupload.claim.model.TableRequestParameters;
import com.healspan.claimservice.mstupload.repo.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private ChronicIllnessMstRepo chronicIllnessMstRepo;

    @Autowired
    private MandatoryDocumentsMstRepo mandatoryDocumentsMstRepo;

    @Autowired
    private ClaimStageMstRepo claimStageMstRepo;

    @Autowired
    private DiagnosisMstRepo diagnosisMstRepo;

    @Autowired
    private GenderMstRepo genderMstRepo;

    @Autowired
    private HospitalMstRepo hospitalMstRepo;

    @Autowired
    private InsuranceCompanyMstRepo insuranceCompanyMstRepo;

    @Autowired
    private OtherCostsMstRepo otherCostsMstRepo;

    @Autowired
    private ProcedureMstRepo procedureMstRepo;

    @Autowired
    private RelationshipMstRepo relationshipMstRepo;

    @Autowired
    private RoomCategoryMstRepo roomCategoryMstRepo;

    @Autowired
    private SpecialityMstRepo specialityMstRepo;

    @Autowired
    private StatusMstRepo statusMstRepo;

    @Autowired
    private TpaMstRepo tpaMstRepo;

    @Autowired
    private TreatmentTypeMstRepo treatmentTypeMstRepo;

    @Autowired
    private UserMstRepo userMstRepo;
    @Autowired
    private UserRoleMstRepo userRoleMstRepo;


    @Autowired
    private StageAndDocumentLinkMstRepo stageAndDocumentLinkMstRepo;

//    private Map<String, JpaRepository> repositoryMap;
//    private Map<String,List<String>> resultMap = new HashMap<>();
//
//    public Map<String, List<String>> getMasterTableData() {
//        constructRepoMap();
//        for(Map.Entry<String,JpaRepository> entry : repositoryMap.entrySet()){
//            List all = entry.getValue().findAll();
//            if (all.size() > 0) {
//                List allEntries = (List) all.stream().map(x -> x.toString()).collect(Collectors.toList());
//                resultMap.put(entry.getKey(), allEntries);
//            }
////            repositoryList.put(entry.getKey(), all.stream().map(String::toString).collect(Collectors.toList());
////                        .forEach(e -> e.getId() +":"+e.getName()).collect(Collectors.toList()));
//        }
//        return resultMap;
//    }
    private Map<String, JpaRepository> repositoryMap;
    private final Map<String, List> repositoryList = new HashMap<>();

    public Map<String, List> getMasterTableData() {
        logger.debug("AdminServiceImpl::getMasterTableData --Start");
        constructRepoMap();
        for (Map.Entry<String, JpaRepository> entry : repositoryMap.entrySet()) {
            List all = entry.getValue().findAll();
            if (all.size() > 0)
                repositoryList.put(entry.getKey(), all);
        }
        addStageWiseMandatoryDocuments(repositoryList);
        logger.debug("AdminServiceImpl::getMasterTableData responseData: {} --End",repositoryList);
        return repositoryList;
    }

    public Map<String, Object> getMasterTableDataName(String tableName) {
        logger.debug("AdminServiceImpl::getMasterTableDataName requestedTableName: {} --Start",tableName);
        Map<String, Object> masterObj = new HashMap<>();
        constructRepoMapOfTable(tableName);
        logger.info("Successfully Mapped masterData in hashMap for masterTable:{}", tableName);
        for (Map.Entry<String, JpaRepository> entry : repositoryMap.entrySet()) {
            if(entry.getKey().equals("user_mst")) {
               List<UserMst> userMst =  entry.getValue().findAll();
               ArrayList<UserMstDtoNew> userMstDtosNew = new ArrayList<>();
               for(UserMst userMst1 : userMst) {
                   UserMstDtoNew userMstDtoNew = new UserMstDtoNew();
                   userMstDtoNew.setId(userMst1.getId());
                   userMstDtoNew.setFirstName(userMst1.getFirstName());
                   userMstDtoNew.setMiddleName(userMst1.getMiddleName());
                   userMstDtoNew.setLastName(userMst1.getLastName());
                   userMstDtoNew.setMobileNo(userMst1.getMobileNo());
                   userMstDtoNew.setUserName(userMst1.getUserName());
                   userMstDtoNew.setPassword(userMst1.getPassword());
                   userMstDtoNew.setEmail(userMst1.getEmail());
                   userMstDtoNew.setActive(userMst1.isActive());
                   userMstDtoNew.setAdminName(userMst1.getUserRoleMst().getName());
                   if(userMst1.getHospitalMst() != null) {
                       userMstDtoNew.setHospitalName(userMst1.getHospitalMst().getName());
                   }
                   userMstDtosNew.add(userMstDtoNew);
               }
                masterObj.put(tableName,userMstDtosNew);
            }else {
                masterObj.put(tableName, entry.getValue().findAll());
                //masterObj  =  entry.getValue().findAll();
            }
        }
        logger.debug("AdminServiceImpl::getMasterTableDataName responseData: {} --End",masterObj);
        return masterObj;
    }

    @Override
    public String deleteMasterDataById(String tableName, Long id) {
        logger.debug("AdminServiceImpl::deleteMasterDataById requested tableName: {} and id: {} --Start",tableName,id);
        constructRepoMapOfTable(tableName);
        logger.info("Successfully Mapped masterData in hashMap for masterTable:{}", tableName);
        for (Map.Entry<String, JpaRepository> entry : repositoryMap.entrySet()) {
            entry.getValue().deleteById(id);
        }
        logger.debug("AdminServiceImpl::deleteMasterDataById responseData: {} --End",id.toString());
        return id.toString();
    }

    public boolean insertMstData(MasterDetails masterDetails) {
        logger.debug("AdminServiceImpl::insertMstData requested masterDetails : {} --Start",masterDetails);
        boolean status = false;
        String tableName = masterDetails.getTableName();
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        if (tableName.equalsIgnoreCase("chronic_illness_mst")) {
            ChronicIllnessMst chronicIllnessMst = new ChronicIllnessMst();
            chronicIllnessMst.setName(parameters.getName());
            chronicIllnessMstRepo.save(chronicIllnessMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("claim_stage_mst")) {
            ClaimStageMst claimStageMstDao = new ClaimStageMst();
            claimStageMstDao.setName(parameters.getName());
            claimStageMstRepo.save(claimStageMstDao);
            status = true;
        } else if (tableName.equalsIgnoreCase("diagnosis_mst")) {
            DiagnosisMst diagnosisMstDoa = new DiagnosisMst();
            diagnosisMstDoa.setName(parameters.getName());
            diagnosisMstRepo.save(diagnosisMstDoa);
            status = true;
        } else if (tableName.equalsIgnoreCase("gender_mst")) {
            GenderMst genderMst = new GenderMst();
            genderMst.setName(parameters.getName());
            genderMstRepo.save(genderMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("hospital_mst")) {
            HospitalMst hospitalMst = new HospitalMst();
            hospitalMst.setName(parameters.getName());
            hospitalMstRepo.save(hospitalMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("insurance_company_mst")) {
            InsuranceCompanyMst insuranceCompanyMst = new InsuranceCompanyMst();
            insuranceCompanyMst.setName(parameters.getName());
            insuranceCompanyMstRepo.save(insuranceCompanyMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("other_costs_mst")) {
            OtherCostsMst otherCostsMst = new OtherCostsMst();
            otherCostsMst.setName(parameters.getName());
            otherCostsMstRepo.save(otherCostsMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("procedure_mst")) {
            ProcedureMst procedureMst = new ProcedureMst();
            procedureMst.setName(parameters.getName());
            procedureMstRepo.save(procedureMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("relationship_mst")) {
            RelationshipMst relationshipMst = new RelationshipMst();
            relationshipMst.setName(parameters.getName());
            relationshipMstRepo.save(relationshipMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("room_category_mst")) {
            RoomCategoryMst roomCategoryMst = new RoomCategoryMst();
            roomCategoryMst.setName(parameters.getName());
            roomCategoryMstRepo.save(roomCategoryMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("speciality_mst")) {
            SpecialityMst specialityMst = new SpecialityMst();
            specialityMst.setName(parameters.getName());
            specialityMstRepo.save(specialityMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("tpa_mst")) {
            TpaMst tpaMst = new TpaMst();
            tpaMst.setName(parameters.getName());
            tpaMstRepo.save(tpaMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("treatment_type_mst")) {
            TreatmentTypeMst treatmentTypeMst = new TreatmentTypeMst();
            treatmentTypeMst.setName(parameters.getName());
            treatmentTypeMstRepo.save(treatmentTypeMst);
            status = true;
        } else if (tableName.equalsIgnoreCase("user_role_mst")) {
            UserRoleMst userRoleMst = new UserRoleMst();
            userRoleMst.setName(parameters.getName());
            userRoleMstRepo.save(userRoleMst);
            status = true;

        } else if (tableName.equalsIgnoreCase("mandatory_documents_mst")) {
            repositoryMap.put("mandatory_documents_mst", mandatoryDocumentsMstRepo);
        } else if (tableName.equalsIgnoreCase("user_mst")) {
            UserMst userMst = new UserMst();
            userMst.setUserName(parameters.getUserName());
            userMst.setFirstName(parameters.getFirstName());
            userMst.setLastName(parameters.getLastName());
            userMst.setHospitalMst(hospitalMstRepo.findById(parameters.getHospitalMstId()).get());
            userMst.setUserRoleMst(userRoleMstRepo.findById(parameters.getUserRoleMstId()).get());
            userMstRepo.save(userMst);
            status = true;
        } else {
            repositoryMap.put(" Data is not present", null);
        }
        logger.debug("AdminServiceImpl::insertMstData response status : {} --End",status);
        return status;
    }

    public boolean updateMasterData(MasterDetails masterDetails) {
        logger.debug("AdminServiceImpl::updateMasterData requested masterDetails: {} --Start",masterDetails);
        boolean status = false;
        String tableName = masterDetails.getTableName();
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        if (tableName.equalsIgnoreCase("chronic_illness_mst")) {
            Optional<ChronicIllnessMst> chronicIllnessMstDao = chronicIllnessMstRepo.findById(parameters.getId());
            if (chronicIllnessMstDao.isPresent()) {
                chronicIllnessMstDao.get().setName(parameters.getName());
                chronicIllnessMstRepo.save(chronicIllnessMstDao.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("claim_stage_mst")) {
            Optional<ClaimStageMst> claimStageMstDAO = claimStageMstRepo.findById(parameters.getId());
            if (claimStageMstDAO.isPresent()) {
                claimStageMstDAO.get().setName(parameters.getName());
                claimStageMstRepo.save(claimStageMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("diagnosis_mst")) {
            Optional<DiagnosisMst> diagnosisMstDAO = diagnosisMstRepo.findById(parameters.getId());
            if (diagnosisMstDAO.isPresent()) {
                diagnosisMstDAO.get().setName(parameters.getName());
                diagnosisMstRepo.save(diagnosisMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("gender_mst")) {
            Optional<GenderMst> genderMstDAO = genderMstRepo.findById(parameters.getId());
            if (genderMstDAO.isPresent()) {
                genderMstDAO.get().setName(parameters.getName());
                genderMstRepo.save(genderMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("hospital_mst")) {
            Optional<HospitalMst> hospitalMstDAO = hospitalMstRepo.findById(parameters.getId());
            if (hospitalMstDAO.isPresent()) {
                hospitalMstDAO.get().setName(parameters.getName());
                hospitalMstRepo.save(hospitalMstDAO.get());
                status = true;
            }

        } else if (tableName.equalsIgnoreCase("insurance_company_mst")) {
            Optional<InsuranceCompanyMst> insuranceCompanyMstDAO = insuranceCompanyMstRepo.findById(parameters.getId());
            if (insuranceCompanyMstDAO.isPresent()) {
                insuranceCompanyMstDAO.get().setName(parameters.getName());
                insuranceCompanyMstRepo.save(insuranceCompanyMstDAO.get());
                status = true;
            }

        } else if (tableName.equalsIgnoreCase("other_costs_mst")) {
            Optional<OtherCostsMst> otherCostsMstDAO = otherCostsMstRepo.findById(parameters.getId());
            if (otherCostsMstDAO.isPresent()) {
                otherCostsMstDAO.get().setName(parameters.getName());
                otherCostsMstRepo.save(otherCostsMstDAO.get());
                status = true;
            }

        } else if (tableName.equalsIgnoreCase("procedure_mst")) {
            Optional<ProcedureMst> procedureMstDAO = procedureMstRepo.findById(parameters.getId());
            if (procedureMstDAO.isPresent()) {
                procedureMstDAO.get().setName(parameters.getName());
                procedureMstRepo.save(procedureMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("relationship_mst")) {
            Optional<RelationshipMst> relationshipMstDAO = relationshipMstRepo.findById(parameters.getId());
            if (relationshipMstDAO.isPresent()) {
                relationshipMstDAO.get().setName(parameters.getName());
                relationshipMstRepo.save(relationshipMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("room_category_mst")) {
            Optional<RoomCategoryMst> roomCategoryMstDAO = roomCategoryMstRepo.findById(parameters.getId());
            if (roomCategoryMstDAO.isPresent()) {
                roomCategoryMstDAO.get().setName(parameters.getName());
                roomCategoryMstRepo.save(roomCategoryMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("speciality_mst")) {
            Optional<SpecialityMst> specialityMstDAO = specialityMstRepo.findById(parameters.getId());
            if (specialityMstDAO.isPresent()) {
                specialityMstDAO.get().setName(parameters.getName());
                specialityMstRepo.save(specialityMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("tpa_mst")) {
            Optional<TpaMst> tpaMstDAO = tpaMstRepo.findById(parameters.getId());
            if (tpaMstDAO.isPresent()) {
                tpaMstDAO.get().setName(parameters.getName());
                tpaMstRepo.save(tpaMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("treatment_type_mst")) {
            Optional<TreatmentTypeMst> treatmentTypeMstDAO = treatmentTypeMstRepo.findById(parameters.getId());
            if (treatmentTypeMstDAO.isPresent()) {
                treatmentTypeMstDAO.get().setName(parameters.getName());
                treatmentTypeMstRepo.save(treatmentTypeMstDAO.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("user_role_mst")) {
            Optional<UserRoleMst>UserRoleMstDto = userRoleMstRepo.findById(parameters.getId());
            if (UserRoleMstDto.isPresent()){
                UserRoleMstDto.get().setName(parameters.getName());
                userRoleMstRepo.save(UserRoleMstDto.get());
                status = true;
            }
        } else if (tableName.equalsIgnoreCase("mandatory_documents_mst")) {
            repositoryMap.put("mandatory_documents_mst", mandatoryDocumentsMstRepo);
        } else if (tableName.equalsIgnoreCase("user_mst")) {
            Optional<UserMst> userMstDAO = userMstRepo.findById(parameters.getId());
            if (userMstDAO.isPresent()) {
                userMstDAO.get().setUserName(parameters.getUserName());
                userMstDAO.get().setFirstName(parameters.getFirstName());
                userMstDAO.get().setLastName(parameters.getLastName());
            /*userMstDAO.get().setHospitalMst(parameters.getHospitalMstId());
            userMstDAO.setHospitalMst(hospitalMstRepo.findById(parameters.getHospitalMstId()).get());
            userMst.setUserRoleMst(userRoleMstRepo.findById(parameters.getUserRoleMstId()).get());*/
                userMstRepo.save(userMstDAO.get());
                status = true;
            }
        } else {
            repositoryMap.put(" Data is not present", null);
        }
        logger.debug("AdminServiceImpl::updateMasterData response Status: {} --End",status);
        return status;
    }

    private void constructRepoMapOfTable(String tableName) {
        logger.debug("AdminServiceImpl::constructRepoMapOfTable requested tableName: {} --Start",tableName);
        repositoryMap = new HashMap<>();
        logger.info("Mapping requested masterData in hashMap for table:{}", tableName);
        if (tableName.equalsIgnoreCase("chronic_illness_mst")) {
            repositoryMap.put("chronic_illness_mst", chronicIllnessMstRepo);
        } else if (tableName.equalsIgnoreCase("claim_stage_mst")) {
            repositoryMap.put("claim_stage_mst", claimStageMstRepo);
        } else if (tableName.equalsIgnoreCase("diagnosis_mst")) {
            repositoryMap.put("diagnosis_mst", diagnosisMstRepo);
        } else if (tableName.equalsIgnoreCase("gender_mst")) {
            repositoryMap.put("gender_mst", genderMstRepo);
        } else if (tableName.equalsIgnoreCase("hospital_mst")) {
            repositoryMap.put("hospital_mst", hospitalMstRepo);
        } else if (tableName.equalsIgnoreCase("insurance_company_mst")) {
            repositoryMap.put("insurance_company_mst", insuranceCompanyMstRepo);
        } else if (tableName.equalsIgnoreCase("other_costs_mst")) {
            repositoryMap.put("other_costs_mst", otherCostsMstRepo);
        } else if (tableName.equalsIgnoreCase("procedure_mst")) {
            repositoryMap.put("procedure_mst", procedureMstRepo);
        } else if (tableName.equalsIgnoreCase("relationship_mst")) {
            repositoryMap.put("relationship_mst", relationshipMstRepo);
        } else if (tableName.equalsIgnoreCase("room_category_mst")) {
            repositoryMap.put("room_category_mst", roomCategoryMstRepo);
        } else if (tableName.equalsIgnoreCase("status_mst")) {
            repositoryMap.put("status_mst", statusMstRepo);
        } else if (tableName.equalsIgnoreCase("tpa_mst")) {
            repositoryMap.put("tpa_mst", tpaMstRepo);
        } else if (tableName.equalsIgnoreCase("treatment_type_mst")) {
            repositoryMap.put("treatment_type_mst", treatmentTypeMstRepo);
        } else if (tableName.equalsIgnoreCase("user_role_mst")) {
            repositoryMap.put("user_role_mst", userRoleMstRepo);
        } else if (tableName.equalsIgnoreCase("mandatory_documents_mst")) {
            repositoryMap.put("mandatory_documents_mst", mandatoryDocumentsMstRepo);
        } else if (tableName.equalsIgnoreCase("user_mst")) {
            repositoryMap.put("user_mst", userMstRepo);
        } else
            repositoryMap.put("not present", null);
        logger.debug("AdminServiceImpl::constructRepoMapOfTable requested tableName: {} --End",tableName);
    }

    private void constructRepoMap() {
        logger.debug("AdminServiceImpl::constructRepoMap requested --Start");
        if (repositoryMap == null) {
            logger.info("Mapping all repo object in hashmap: --Start");
            repositoryMap = new HashMap<>();
            repositoryMap.put("chronic_illness_mst", chronicIllnessMstRepo);
            repositoryMap.put("claim_stage_mst", claimStageMstRepo);
            repositoryMap.put("diagnosis_mst", diagnosisMstRepo);
            repositoryMap.put("gender_mst", genderMstRepo);
            repositoryMap.put("hospital_mst", hospitalMstRepo);
            repositoryMap.put("insurance_company_mst", insuranceCompanyMstRepo);
            repositoryMap.put("other_costs_mst", otherCostsMstRepo);
            repositoryMap.put("procedure_mst", procedureMstRepo);
            repositoryMap.put("relationship_mst", relationshipMstRepo);
            repositoryMap.put("room_category_mst", roomCategoryMstRepo);
            repositoryMap.put("speciality_mst", specialityMstRepo);
            repositoryMap.put("status_mst", statusMstRepo);
            repositoryMap.put("tpa_mst", tpaMstRepo);
            repositoryMap.put("treatment_type_mst", treatmentTypeMstRepo);
            repositoryMap.put("user_role_mst", userRoleMstRepo);
            repositoryMap.put("mandatory_documents_mst", mandatoryDocumentsMstRepo);
            repositoryMap.put("user_mst", userMstRepo);
        }
        logger.debug("AdminServiceImpl::constructRepoMap requested --End");
    }

    private void addStageWiseMandatoryDocuments(Map<String, List> map) {
        logger.debug("AdminServiceImpl::addStageWiseMandatoryDocuments requested Data: {} --Start",map);
        logger.debug("Find All stageAndDocumentsLink records from db");
        List<StageAndDocumentLinkMst> stageAndDocumentLinkMstDaoList = stageAndDocumentLinkMstRepo.findAll();
        logger.info("Successfully retrieved All stageAndDocumentsLink records from db");
        List<StageAndDocumentLinkMst> initialStageLinkMstDaoList =
                stageAndDocumentLinkMstDaoList.stream().filter(linkObj -> linkObj.getClaimStageMst().getName().equalsIgnoreCase("Initial Authorization")).collect(Collectors.toList());
        List<StageAndDocumentLinkMst> enhancementStageLinkMstDaoList =
                stageAndDocumentLinkMstDaoList.stream().filter(linkObj -> linkObj.getClaimStageMst().getName().equalsIgnoreCase("Enhancement")).collect(Collectors.toList());
        List<StageAndDocumentLinkMst> dischargeStageLinkMstDaoList =
                stageAndDocumentLinkMstDaoList.stream().filter(linkObj -> linkObj.getClaimStageMst().getName().equalsIgnoreCase("Discharge")).collect(Collectors.toList());
        List<StageAndDocumentLinkMst> finalClaimStageLinkMstDaoList =
                stageAndDocumentLinkMstDaoList.stream().filter(linkObj -> linkObj.getClaimStageMst().getName().equalsIgnoreCase("Final Claim")).collect(Collectors.toList());
        List initialStageDocList = initialStageLinkMstDaoList.stream().map(obj -> obj.getMandatoryDocumentsMst()).collect(Collectors.toList());
        List enhancementStageDocList = enhancementStageLinkMstDaoList.stream().map(obj -> obj.getMandatoryDocumentsMst()).collect(Collectors.toList());
        List dischargeStageDocList = dischargeStageLinkMstDaoList.stream().map(obj -> obj.getMandatoryDocumentsMst()).collect(Collectors.toList());
        List finalStageDocList = finalClaimStageLinkMstDaoList.stream().map(obj -> obj.getMandatoryDocumentsMst()).collect(Collectors.toList());
        map.put("initial_stage", initialStageDocList);
        map.put("enhancement_stage", enhancementStageDocList);
        map.put("discharge_stage", dischargeStageDocList);
        map.put("final_stage", finalStageDocList);
        logger.info("AdminServiceImpl::addStageWiseMandatoryDocuments  --End");
    }
}
