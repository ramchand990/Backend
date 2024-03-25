package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.client.RpaClient;
import com.healspan.claimservice.mstupload.claim.constants.Constant;
import com.healspan.claimservice.mstupload.claim.dao.business.*;
import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.MandatoryDocumentsMst;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import com.healspan.claimservice.mstupload.claim.dto.business.*;
import com.healspan.claimservice.mstupload.claim.jdbcrepo.ClaimRepo;
import com.healspan.claimservice.mstupload.claim.model.*;
import com.healspan.claimservice.mstupload.claim.repo.ClaimAssignmentRepo;
import com.healspan.claimservice.mstupload.claim.repo.ClaimInfoRepo;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import com.healspan.claimservice.mstupload.claim.repo.DocumentRepo;
import com.healspan.claimservice.mstupload.claim.util.*;
import com.healspan.claimservice.mstupload.repo.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.healspan.claimservice.mstupload.claim.constants.Constant.RequestType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.lang3.exception.ExceptionUtils;

@Component
public class ClaimOperationServiceImpl implements ClaimOperationService {

    private Logger logger = LoggerFactory.getLogger(ClaimOperationServiceImpl.class);
    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;
    @Autowired
    private ClaimStageMstRepo claimStageMstRepo;
    @Autowired
    private S3FileService s3FileService;
    @Autowired
    private ClaimUtil claimUtil;
    @Autowired
    private PatientInfoUtil patientInfoUtil;
    @Autowired
    private MedicalInfoUtil medicalInfoUtil;
    @Autowired
    private InsuranceInfoUtil insuranceInfoUtil;
    @Autowired
    private QuestionnairesUtil questionnairesUtil;
    @Autowired
    private DocumentUtil documentUtil;
    @Autowired
    private StatusMstRepo statusMstRepo;
    @Autowired
    private UserMstRepo userMstRepo;
    @Autowired
    private ClaimAssignmentService assignmentService;
    @Autowired
    private RpaClient rpaClient;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ClaimStageLinkUtil claimStageLinkUtil;
    @Autowired
    private MandatoryDocumentsMstRepo mandatoryDocumentsMstRepo;
    @Autowired
    private ClaimInfoRepo claimInfoRepo;
    @Autowired
    private ClaimAssignmentRepo claimAssignmentRepo;
    @Autowired
    private SlaUtil slaUtil;
    @Autowired
    private ClaimRepo claimRepo;
    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private ClaimAssignmentUtil claimAssignmentUtil;
    @Autowired
    private UpdateClaimAssignmentService updateClaimAssignmentService;

    @Override
    public ResourceCreationResponse createOrUpdateClaimInfo(ClaimInfoDto dto) {
        logger.debug("ClaimOperationServiceImpl::createOrUpdateClaimInfo requestedData: {} --Start", dto);
        ResourceCreationResponse response = new ResourceCreationResponse();
        try {
            ClaimStageLink claimStageLinkDao = claimStageLinkUtil.getClaimStageLink(dto);
            if (CommonUtil.isObjectNotNull.test(dto)) {
                ClaimInfo claimInfoDao = claimUtil.saveOrUpdateClaim(dto);
                claimStageLinkDao.setClaimInfo(claimInfoDao);
                response.setClaimInfoId(claimInfoDao.getId());
            }
            if (CommonUtil.isObjectNotNull.test(dto.getPatientInfoDto())) {
                PatientInfo patientInfoDao = patientInfoUtil.saveOrUpdatePatientInfo(dto.getPatientInfoDto());
                claimStageLinkDao.setPatientInfo(patientInfoDao);
                response.setPatientInfoId(patientInfoDao.getId());
            }
            logger.debug("Save claimStageLink records in db");
            claimStageLinkRepo.save(claimStageLinkDao);
            if (RequestType.CREATE.name().equalsIgnoreCase(dto.getRequestType())) {
                patientInfoUtil.insertInitialRecordInClaimAssignment(dto.getUserId(), claimStageLinkDao);
                logger.info("Initial Record Inserted In ClaimAssignment Table::claimInfoId::{}", dto.getId());
            }
            response.setClaimStageLinkId(claimStageLinkDao.getId());
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(Constant.ResponseStatus.FAILED);
            return response;
        }
        response.setResponseStatus(Constant.ResponseStatus.SUCCESS);
        logger.debug("ClaimOperationServiceImpl::createOrUpdateClaimInfo responseData: {} --End", response);
        return response;
    }

    @Override
    public ResourceCreationResponse createOrUpdateMedicalInfo(MedicalInfoDto dto) {
        logger.debug("ClaimOperationServiceImpl::createOrUpdateMedicalInfo requestedData: {} --Start", dto);
        MedicalInfo medicalInfoDao = null;
        ResourceCreationResponse response = new ResourceCreationResponse();
        logger.debug("Find claimStageLink record in db for claimStageLinkId:{}", dto.getClaimStageLinkId());
        ClaimStageLink claimStageLinkFromDb = claimStageLinkRepo.findById(dto.getClaimStageLinkId()).get();
        try {
            if (CommonUtil.isObjectNotNull.test(dto)) {
                medicalInfoDao = medicalInfoUtil.saveOrUpdateMedicalInfo(dto);
                claimStageLinkFromDb.setMedicalInfo(medicalInfoDao);
                response.setMedicalInfoId(medicalInfoDao.getId());
            }
            logger.debug("Save claimStageLink record for claimStageLinkIs:{}", claimStageLinkFromDb.getId());
            claimStageLinkRepo.save(claimStageLinkFromDb);
            response.setClaimStageLinkId(claimStageLinkFromDb.getId());
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(Constant.ResponseStatus.FAILED);
            return response;
        }
        response.setResponseStatus(Constant.ResponseStatus.SUCCESS);
        logger.debug("ClaimOperationServiceImpl::createOrUpdateMedicalInfo responseData: {} --End", response);
        return response;
    }

    @Override
    public ResourceCreationResponse createOrUpdateInsuranceInfo(InsuranceInfoDto dto) {
        logger.debug("ClaimOperationServiceImpl::createOrUpdateInsuranceInfo requestedData: {} --Start", dto);
        InsuranceInfo insuranceInfoDao = null;
        ResourceCreationResponse response = new ResourceCreationResponse();
        logger.debug("Find claimStageLink record from db for claimStageLinkId:{}", dto.getClaimStageLinkId());
        ClaimStageLink claimStageLinkFromDb = claimStageLinkRepo.findById(dto.getClaimStageLinkId()).get();
        try {
            if (CommonUtil.isObjectNotNull.test(dto)) {
                insuranceInfoDao = insuranceInfoUtil.saveOrUpdateInsuranceInfo(dto);
                claimStageLinkFromDb.setInsuranceInfo(insuranceInfoDao);
                response.setInsuranceInfoId(insuranceInfoDao.getId());
            }
            logger.info("Save claimStageLink record in db for claimStageLinkId:{}", claimStageLinkFromDb.getId());
            claimStageLinkRepo.save(claimStageLinkFromDb);
            response.setClaimStageLinkId(claimStageLinkFromDb.getId());
        } catch (Exception e) {
            logger.error("Exception Details: \n {}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(Constant.ResponseStatus.FAILED);
            return response;
        }
        response.setResponseStatus(Constant.ResponseStatus.SUCCESS);
        logger.debug("ClaimOperationServiceImpl::createOrUpdateInsuranceInfo responseData: {} --End", response);
        return response;
    }

    public DashboardDataDto findAllClaimCreatedByUserAndName(Long userId, String inputData) {
        logger.debug("ClaimOperationServiceImpl::findAllClaimCreatedByUserAndName requestedUserId: {} and inputData: {} --Start", userId, inputData);
        DashboardDataDto dashboardDataDto = new DashboardDataDto();
        List<LoggedInUserClaimData> loggedInUserClaimDataList = null;
        List<ClaimStageLink> claimInfoDaoList = null;

        //Check whether the received input is String or number
        Long claimId = null;
        String name = null;
        try {
            claimId = Long.parseLong(inputData);
        } catch (NumberFormatException nfe) {
            name = inputData;
        }
        if (claimId != null) {
            logger.debug("find all claim stage link records from db for claimId: {}", claimId);
            claimInfoDaoList = claimStageLinkRepo.findAllByClaimInfoId(claimId);
        } else {
            logger.debug("find all claim stage link ids records from db for userId: {}", userId);
            List<Long> claimStageLinkIdList = claimStageLinkRepo.findAllByUserMstId(userId);
            logger.debug("find all claim stage link records from db for all claimStageLinkId: {}", claimStageLinkIdList);
            claimInfoDaoList = claimStageLinkRepo.findAllByIdIn(claimStageLinkIdList);
        }

        try {
            logger.debug("findAllClaimCreatedByUserAndName:{}", claimId + name);
            loggedInUserClaimDataList = claimUtil.findAllClaimCreatedByUserAndName(claimInfoDaoList, claimId, name);
            if (!loggedInUserClaimDataList.isEmpty()) {
                logger.debug("retrieve deriveClaimStageDraftCount: {}", loggedInUserClaimDataList);
                dashboardDataDto.setClaimStagePendingCount(claimUtil.deriveClaimStageDraftData(loggedInUserClaimDataList));
                logger.debug("retrieve deriveClaimStageApprovalCount: {}", loggedInUserClaimDataList);
                dashboardDataDto.setClaimStageApprovalCount(claimUtil.deriveClaimStageApprovalData(loggedInUserClaimDataList));
            }
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimOperationServiceImpl::findAllClaimCreatedByUserAndName responseData: {} --End", dashboardDataDto);
        return dashboardDataDto;
    }


    @Override
    public ResourceCreationResponse createOrUpdateQuestionnairesAndFileData(QuestionnairesAndFileDataDto dto) {
        logger.debug("ClaimOperationServiceImpl::createOrUpdateQuestionnairesAndFileData requestedData: {} --Start", dto);
        ResourceCreationResponse response = new ResourceCreationResponse();
        try {
            logger.debug("Find the claim info record from db for ClaimInfoId(): {}", dto.getClaimStageLinkId());
            Optional<ClaimStageLink> claimStageLinkDao = claimStageLinkRepo.findById(dto.getClaimStageLinkId());
            logger.debug("save questionnaires data for Sequential Question and claimInfoId: {}", claimStageLinkDao.get().getId());
            questionnairesUtil.saveQuestionnairesData(dto.getSequentialQuestion(), claimStageLinkDao.get());
            logger.debug("save document data for document list and claimInfoId: {}", claimStageLinkDao.get().getId());
            Map<Long, Long> documentMap = documentUtil.saveDocumentData(dto.getDocumentIdList(), claimStageLinkDao.get());
            response.setDocumentId(documentMap);
        } catch (Exception e) {
            logger.error("Exception Details: \n {}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(Constant.ResponseStatus.FAILED);
            return response;
        }
        response.setResponseStatus(Constant.ResponseStatus.SUCCESS);
        logger.debug("ClaimOperationServiceImpl::createOrUpdateQuestionnairesAndFileData responseData: {} --End", response);
        return response;
    }

    @Override
    public ResponseClaimStatusDto createClaimAsDraft(ClaimInfoDto dto) {
        logger.debug("ClaimOperationServiceImpl::createClaimAsDraft requestedData: {} --Start", dto);
        ResponseClaimStatusDto response = new ResponseClaimStatusDto();
        response.setClaimId(dto.getId());
        response.setClaimStageLinkId(dto.getClaimStageLinkId());
        ClaimInfo claimInfoDao = null;
        PatientInfo patientInfoDao = null;
        MedicalInfo medicalInfoDao = null;
        InsuranceInfo insuranceInfoDao = null;
        ClaimStageLink claimStageLinkDao = new ClaimStageLink();
        try {
            logger.debug("Find the Claim Stage record from db for ClaimStageId: {}", dto.getClaimStageId());
            ClaimStageMst claimStageMstDao = claimStageMstRepo.findById(dto.getClaimStageId()).get();
            claimStageLinkDao.setClaimStageMst(claimStageMstDao);
            logger.debug("Find the Status record from db for statusId: {}", dto.getStatusId());
            StatusMst statusMstDao = statusMstRepo.findById(dto.getStatusId()).get();
            claimStageLinkDao.setStatusMst(statusMstDao);
            logger.debug("Find the User record from db for userId: {}", dto.getUserId());
            UserMst userMstDao = userMstRepo.findById(dto.getUserId()).get();
            claimStageLinkDao.setUserMst(userMstDao);
            claimStageLinkDao.setCreatedDateTime(LocalDateTime.now());

            if (CommonUtil.isObjectNotNull.test(dto)) {
                logger.debug("save or update the Claim info for claimInfoId:{}", dto.getId());
                claimInfoDao = claimUtil.saveOrUpdateClaim(dto);
                claimStageLinkDao.setClaimInfo(claimInfoDao);
            }
            if (CommonUtil.isObjectNotNull.test(dto.getPatientInfoDto())) {
                logger.debug("save or update the updated Patient info for patientId:{}", dto.getPatientInfoDto().getId());
                patientInfoDao = patientInfoUtil.saveOrUpdatePatientInfo(dto.getPatientInfoDto());
                claimStageLinkDao.setPatientInfo(patientInfoDao);
            }
            /** NEXT-1 **/
            if (CommonUtil.isObjectNotNull.test(dto.getMedicalInfoDto())) {
                logger.debug("save or update the updated Medical info for MedicalId:{}", dto.getMedicalInfoDto().getId());
                medicalInfoDao = medicalInfoUtil.saveOrUpdateMedicalInfo(dto.getMedicalInfoDto());
                claimStageLinkDao.setMedicalInfo(medicalInfoDao);
            }
            /** NEXT-2 **/
            if (CommonUtil.isObjectNotNull.test(dto.getInsuranceInfoDto())) {
                logger.debug("save or update the updated Insurance info for insuranceId:{}", dto.getInsuranceInfoDto().getId());
                insuranceInfoDao = insuranceInfoUtil.saveOrUpdateInsuranceInfo(dto.getInsuranceInfoDto());
                claimStageLinkDao.setInsuranceInfo(insuranceInfoDao);
            }
            /** NEXT-3 **/
            logger.debug("Find Claim Stage Link record from db for claimInfoId:{} and claimStageId:{}", claimInfoDao.getId(), dto.getClaimStageId());
            Optional<ClaimStageLink> claimStageLinkFromDb = claimStageLinkRepo.findByClaimInfoIdAndClaimStageMstId(claimInfoDao.getId(), dto.getClaimStageId());
            if (claimStageLinkFromDb.isPresent()) {
                claimStageLinkDao.setId(claimStageLinkFromDb.get().getId());
                logger.debug("Save Claim Stage record in db");
                claimStageLinkRepo.save(claimStageLinkDao);
                logger.debug("Successfully saved Claim Stage record in db for claimStageLinkId:{}", claimStageLinkDao.getId());
            } else {
                logger.info("Save Claim Stage record in db");
                claimStageLinkRepo.save(claimStageLinkDao);
                logger.debug("Successfully saved Claim Stage record in db for claimStageLinkId:{}", claimStageLinkDao.getId());
            }
            response.setResponseStatus(String.valueOf(Constant.ResponseStatus.SUCCESS));
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(e.getMessage());
        }
        logger.debug("ClaimOperationServiceImpl::createClaimAsDraft responseData: {} --End", response);
        return response;
    }

    @Override
    public DashboardDataDto findAllClaimCreatedByUser(Long userId) {
        logger.debug("ClaimOperationServiceImpl::findAllClaimCreatedByUser requestedUserID: {} --Start", userId);
        DashboardDataDto dashboardDataDto = new DashboardDataDto();
        List<LoggedInUserClaimData> loggedInUserClaimDataList = null;
        try {
            logger.debug("Find all claim created by user for userId:{}", userId);
            loggedInUserClaimDataList = claimUtil.findAllClaimCreatedByUser(userId);
            setSlaAgainstClaims(loggedInUserClaimDataList);
            logger.debug("Successfully retrieved all logged in user claim data list: {} ", loggedInUserClaimDataList);
            logger.debug("retrieve derive claim stage pending count for loggedInUserClaimDataList: {}", loggedInUserClaimDataList);
            dashboardDataDto.setClaimStagePendingCount(claimUtil.deriveClaimStageDraftData(loggedInUserClaimDataList));
            logger.debug("retrieve derive claim stage approval count for loggedInUserClaimDataList: {}", loggedInUserClaimDataList);
            dashboardDataDto.setClaimStageApprovalCount(claimUtil.deriveClaimStageApprovalData(loggedInUserClaimDataList));
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimOperationServiceImpl::createClaimAsDraft responseData: {} --End", dashboardDataDto);
        return dashboardDataDto;
    }

    @Override
    public String getHospitalUserDashboardData(long userId) {
        logger.debug("ClaimOperationServiceImpl::getHospitalUserDashboardData requestedUserId: {} --Start",userId);
        String jsonString = "";
        try {
            jsonString = claimRepo.getHospitalUserDashboardDataFromDb(userId);
        } catch (Exception e) {
            logger.error("ClaimOperationServiceImpl::getHospitalUserDashboardData::Exception::{}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimOperationServiceImpl::getHospitalUserDashboardData responseData: {} --End",jsonString);
        return jsonString;
    }

    @Override
    public String getHealspanUserDashboardData(long userId) {
        logger.debug("ClaimOperationServiceImpl::getHealspanUserDashboardData requestedUserId : {} --Start",userId);
        String jsonString = "";
        try {
            jsonString = claimRepo.getHealspanUserDashboardDataFromDb(userId);
        } catch (Exception e) {
            logger.error("ClaimOperationServiceImpl::getHealspanUserDashboardData::Exception::{}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimOperationServiceImpl::getHealspanUserDashboardData responseData: {} --End",jsonString);
        return jsonString;
    }


    public ReviewerDashboardDataDto findAllReviewerClaims(Long userId) {
        logger.debug("ClaimOperationServiceImpl::findAllReviewerClaims requestedUserId: {} --Start", userId);
        ReviewerDashboardDataDto reviewerDashboardDataDto = new ReviewerDashboardDataDto();
        logger.debug("Get all reviewer claim data for userId: {}", userId);
        List<ReviewerClaimsData> reviewerClaimsDataList = claimUtil.getAllReviewerClaims(userId);
        logger.debug("Successfully retrieved all reviewer claim data: {} ", reviewerClaimsDataList);
        if (!reviewerClaimsDataList.isEmpty()) {
            reviewerDashboardDataDto.setReviewerClaimsDataList(reviewerClaimsDataList);
            //Hardcode SLA count to zero for now
            //  reviewerDashboardDataDto.setSlaClaimsCount(new SlaClaimsCount());
        }
        logger.debug("ClaimOperationServiceImpl::findAllReviewerClaims responseData: {} --End", reviewerDashboardDataDto);
        return reviewerDashboardDataDto;
    }

    @Override
    public ClaimStageLinkDto findClaim(Long id) {
        logger.debug("ClaimOperationServiceImpl::findClaim requestedId: {} --Start", id);
        ClaimStageLinkDto claimStageLinkDto = null;
        try {
            logger.debug("Find all claim stage link records from db for claimStageLinkId: {}", id);
            Optional<ClaimStageLink> claimStageLinkDao = claimStageLinkRepo.findById(id);
            if (claimStageLinkDao.isPresent()) {
                claimStageLinkDto = modelMapper.map(claimStageLinkDao.get(), ClaimStageLinkDto.class);
                long claimInfoId = claimStageLinkDto.getClaimInfo().getId();
                long claimStageMstID = claimStageLinkDto.getClaimStageMst().getClaimStageMstId();
                logger.debug("Find Claim Assignment in db for claimInfoId:{} and claimStageMstId: {}", claimInfoId, claimStageMstID);
                ClaimAssignment claimAssignmentsDao = claimAssignmentRepo.findAllByOpenQueryTransaction(claimInfoId, claimStageMstID);
                logger.debug("Successfully retrieved Claim Assignment in db for claimInfoId:{} and claimStageMstId: {}", claimInfoId, claimStageMstID);
                ClaimAssignmentDto claimAssignmentDto = modelMapper.map(claimAssignmentsDao, ClaimAssignmentDto.class);
                List<ClaimAssignment> claimAssignmentDao1 = claimAssignmentRepo.getAllAssigneeComment((claimInfoId));
                List<AssignCommentDto> assignCommentDtoList = new ArrayList<>();
                for (ClaimAssignment claimAssignment : claimAssignmentDao1) {
                    AssignCommentDto assignCommentDto = new AssignCommentDto();
                    assignCommentDto.setClaimStageMstId(claimAssignment.getClaimStageMst().getId());
                    assignCommentDto.setComments(claimAssignment.getAssigneeComments());
                    LocalDateTime localDateTime = LocalDateTime.parse(String.valueOf(claimAssignment.getAssignedDateTime()));
                    localDateTime.toLocalDate();
                    localDateTime.toLocalTime();

                    String dDate = "" + localDateTime.toLocalDate() + " " + localDateTime.toLocalTime();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.mmm");
                    Date cDate = df.parse(dDate);

                    String pattern = "MM-dd-yyyy HH:MM";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format(cDate);

                    assignCommentDto.setAssignedDateTime(date);
                    assignCommentDtoList.add(assignCommentDto);
                }
                claimAssignmentDto.setAssignCommentDto(assignCommentDtoList);
                claimStageLinkDto.setClaimAssignment(claimAssignmentDto);
                long claimAssignmentId = claimAssignmentDto.getId();
                ClaimInfoDto claimInfoDto = claimStageLinkDto.getClaimInfo();
                claimInfoDto.setAssignId(claimAssignmentId);
            }
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimOperationServiceImpl::findClaim responseData: {} --End", claimStageLinkDto);
        return claimStageLinkDto;
    }

    @Override
    public boolean updateClaimStatus(UpdateClaimStatusDto req) {
        logger.debug("ClaimOperationServiceImpl::updateClaimStatus requestData: {} --Start", req);
        boolean status = true;
        try {
            AssignmentDetail detail = updateClaimAssignmentService.prepareAssignmentDetail(modelMapper.map(req, BaseRequestDto.class));
            updateClaimAssignmentService.setNewUserAndStatus(detail);
            updateClaimAssignmentService.insertCommunicationDetailsInCaseAssignment(detail);
            status = true;
        } catch (Exception e) {
            status = false;
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("ClaimOperationServiceImpl::updateClaimStatus responseData: {} --End", status);
        return status;
    }

    @Override
    public ResponseClaimStatusDto pushClaimDataToRpa(PushClaimDataToRpaDto dto) {
        logger.debug("ClaimOperationServiceImpl::pushClaimDataToRpa requestedData: {} --Start", dto);
        ResponseClaimStatusDto response = new ResponseClaimStatusDto();
        response.setClaimId(dto.getClaimId());
        try {
            AssignmentDetail detail = updateClaimAssignmentService.prepareAssignmentDetail(modelMapper.map(dto, BaseRequestDto.class));
            updateClaimAssignmentService.setNewUserAndStatus(detail);
            updateClaimAssignmentService.insertCommunicationDetailsInCaseAssignment(detail);
            logger.debug("get token response for rpaClient");
            TokenResponseDto tokenResponseDto = rpaClient.getToken();
            logger.debug("Push claimData to rpa for claimId:{} and getAccessToken", detail.getClaimStageLink().getId());
            rpaClient.pushClaimDataToRpa(getTokenResponseDto(detail.getClaimStageLink()), tokenResponseDto.getAccess_token());
            response.setResponseStatus(String.valueOf(Constant.ResponseStatus.SUCCESS));
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(e.getMessage());
        }
        logger.debug("ClaimOperationServiceImpl::pushClaimDataToRpa responseData: {} --End", response);
        return response;
    }

    private RpaRequiredDetailDto getTokenResponseDto(ClaimStageLink claimStageLink) {
        logger.info("ClaimOperationServiceImpl::getTokenResponseDto requestedClaimStageLink: {} --Start", claimStageLink);
        PatientInfo patientInfo = claimStageLink.getPatientInfo();
        MedicalInfo medicalInfo = claimStageLink.getMedicalInfo();
        InsuranceInfo insuranceInfo = claimStageLink.getInsuranceInfo();

        RpaRequiredDetailDto rpaRequiredDetailDto = new RpaRequiredDetailDto();
        ItemData itemData = new ItemData();
        SpecificContent sc = new SpecificContent();
        sc.setHealspanClaimId(claimStageLink.getClaimInfo().getId().toString());
        sc.setMemberId(insuranceInfo.getTpaNumber());
        sc.setTpaName(insuranceInfo.getTpaMst().getName());
        sc.setFullName(patientInfo.getFirstName() + " " + patientInfo.getMiddleName() + " " + patientInfo.getLastname());
        sc.setMobileNo(patientInfo.getMobileNo());
        sc.setDateOfAdmission(patientInfo.getDateOfAdmission() + "");
        sc.setDateOfDischarge(patientInfo.getEstimatedDateOfDischarge() + "");
        sc.setDoctorName(medicalInfo.getDoctorName());
        sc.setDoctorRegNumber(medicalInfo.getDoctorRegistrationNumber());
        sc.setRoomType(patientInfo.getRoomCategoryMst().getName());
        sc.setProposedLineOfTreatment(medicalInfo.getTreatmentTypeMst().getName());
        sc.setTreatment(medicalInfo.getProcedureMst().getName());
        sc.setDiagnosis(medicalInfo.getDiagnosisMst().getName());
        /*sc.setDiagnosisMultipleOption();*/
        /*sc.setPackageName();*/
        sc.setRoomRentAndNursingCharges(patientInfo.getCostPerDay() + "");
        /*sc.setConsultation();*/
        sc.setDocList(getDocumentNameAndPath(claimStageLink.getDocumentList()));
        itemData.setSpecificContent(sc);
        rpaRequiredDetailDto.setItemData(itemData);
        logger.debug("ClaimOperationServiceImpl::getTokenResponseDto responseData: {} --End", rpaRequiredDetailDto);
        return rpaRequiredDetailDto;
    }

    @Override
    public ResponseClaimStatusDto updateStage(RequestDto dto) {
        logger.info("ClaimOperationServiceImpl::updateStage requestedData:{} --Start", dto);
        ClaimStageLink newClaimStageLinkDao = null;
        ResponseClaimStatusDto response = new ResponseClaimStatusDto();
        try {
            newClaimStageLinkDao = new ClaimStageLink();
            ClaimStageLink existingClaimStageLink = claimStageLinkRepo.findAllById(dto.getClaimStageLinkId());
            existingClaimStageLink.setLastUpdatedDateTime(LocalDateTime.now());
            newClaimStageLinkDao.setClaimInfo(existingClaimStageLink.getClaimInfo());
            newClaimStageLinkDao.setUserMst(userMstRepo.findById(dto.getUserId()).get());
            newClaimStageLinkDao.setStatusMst(statusMstRepo.findById(dto.getStatusId()).get());
            newClaimStageLinkDao.setClaimStageMst(claimStageMstRepo.findById(dto.getClaimStageId()).get());
            newClaimStageLinkDao.setCreatedDateTime(LocalDateTime.now());
            patientInfoUtil.copyExistingPatientInfo(existingClaimStageLink, newClaimStageLinkDao);
            medicalInfoUtil.copyExistingMedicalInfo(existingClaimStageLink, newClaimStageLinkDao);
            insuranceInfoUtil.copyExistingInsuranceInfo(existingClaimStageLink, newClaimStageLinkDao);
            logger.debug("Save claimStageLink record in db");
            claimStageLinkRepo.save(newClaimStageLinkDao);
            patientInfoUtil.insertInitialRecordInClaimAssignment(newClaimStageLinkDao.getUserMst().getId(), newClaimStageLinkDao);
            response.setClaimId(newClaimStageLinkDao.getClaimInfo().getId());
            response.setClaimStageLinkId(newClaimStageLinkDao.getId());
            response.setResponseStatus(String.valueOf(Constant.ResponseStatus.SUCCESS));
        } catch (Exception e) {
            logger.error("Exception Details: {}", ExceptionUtils.getStackTrace(e));
            response.setResponseStatus(e.getMessage());
        }
        logger.debug("ClaimOperationServiceImpl::updateStage responseData:{} --End", response);
        return response;
    }

    @Override
    public OtherDocumentDto findDocumentId(String docName, Long id) {
        logger.debug("ClaimOperationServiceImpl::findDocumentId requestDocName: {} and id: {} --Start", docName, id);
        OtherDocumentDto otherDocumentDto = new OtherDocumentDto();
        logger.debug("Find mandatoryDocument record for documentName:{}", docName);
        MandatoryDocumentsMst mandatoryDocumentsMst = mandatoryDocumentsMstRepo.findByName(docName);

        if (mandatoryDocumentsMst != null) {
            otherDocumentDto.setId(mandatoryDocumentsMst.getId());
            logger.info("Successfully retrieved Document for documentName:{} and documentId:{}", docName, id);
            return otherDocumentDto;
        } else {
            //Optional<DocumentTypeMst> documentTypeMst = documentTypeMstRepo.findById(id);
            MandatoryDocumentsMst mandatoryDocumentsMst1 = new MandatoryDocumentsMst();
            mandatoryDocumentsMst1.setName(docName);
            mandatoryDocumentsMst1.setDocumentTypeId(id);
            logger.debug("Save mandatoryDocument record in db");
            mandatoryDocumentsMstRepo.save(mandatoryDocumentsMst1);
            MandatoryDocumentsMst mandatoryDocumentsMst2 = mandatoryDocumentsMstRepo.findByName(docName);
            otherDocumentDto.setId(mandatoryDocumentsMst2.getId());
            logger.debug("ClaimOperationServiceImpl::findDocumentId responseData: {} --End", otherDocumentDto);
            return otherDocumentDto;
        }
    }

    public void setSlaAgainstClaims(List<LoggedInUserClaimData> loggedInUserClaimDataList) {
        Map<Long, ClaimSlaDto> slaMap = slaUtil.findAssignmentTransactionsToDeriveSlaAgainstAllClaims(loggedInUserClaimDataList);
        for (LoggedInUserClaimData loggedInUserClaimData : loggedInUserClaimDataList) {
            loggedInUserClaimData.setConsumedSla(slaMap.get(loggedInUserClaimData.getClaimID()));
        }
    }

    @Override
    public ReviewerDashboardDataDto getClaimsForReviewerDashboard(long loggedInUserId) {
        logger.debug("ClaimOperationServiceImpl : getClaimsForReviewerDashboard requestedLoggedInUserId: {} --Start", loggedInUserId);
        ReviewerDashboardDataDto reviewerDashboardDataDto = new ReviewerDashboardDataDto();
        List<ReviewerClaimsData> dtoList = claimRepo.deriveClaimsForReviewerDashboard(loggedInUserId);
        reviewerDashboardDataDto.setReviewerClaimsDataList(dtoList);
        logger.debug("ClaimOperationServiceImpl : getClaimsForReviewerDashboard responseData: {} --End", reviewerDashboardDataDto);
        return reviewerDashboardDataDto;
    }

    @Override
    public DocumentData deleteDocument(long documentId) {
        logger.debug("ClaimOperationServiceImpl::deleteDocument requestedDocumentId:{} --Start", documentId);
        DocumentData documentData = new DocumentData();
        logger.debug("Find Document from db for documentId:{}", documentId);
        Optional<Document> document = documentRepo.findById(documentId);
        if (document.isPresent()) {
            Document documentObj = document.get();
            String path = documentObj.getDocumentPath() + "/" + documentObj.getDocumentName();
            s3FileService.deleteFile(path);
            boolean fileStatus = s3FileService.exists(path);
            logger.debug("Check whether file is present in aws s3 or not ?:{}= ", fileStatus);
            if (!fileStatus) {
                document.get().setDocumentName(null);
                document.get().setDocumentPath(null);
                document.get().setStatus(false);
                logger.debug("Save Document record in db");
                documentRepo.save(document.get());
                documentData.setStatus(true);
                documentData.setResponse("Document details has been deleted for requested id: " + documentId);
            } else {
                documentData.setResponse("Unable to delete document from aws s3: " + documentObj);
            }
        } else {
            documentData.setResponse("Document details not present for requested id: " + documentId);
        }
        logger.debug("ClaimOperationServiceImpl::deleteDocument responseData: {} --End", documentData);
        return documentData;
    }

    private String getDocumentNameAndPath(List<Document> documentList) {
        logger.debug("ClaimOperationServiceImpl::getDocumentNameAndPath requestedDocumentList: {}",documentList.toString());
        List<String> docList = new ArrayList<>();

        for (Document document : documentList) {
            String message = document.getDocumentPath() + "/" + document.getDocumentName();
            docList.add(message);
        }
        logger.debug("ClaimOperationServiceImpl::getDocumentNameAndPath responseData: {}",docList.toString());
        return docList.toString();
    }

    /*private String[] getDocumentNameAndPath(List<Document> documentList){
        int length = documentList.size();
        List<String> docList = new ArrayList<>();
        for(Document document : documentList){
            String message = document.getDocumentPath()+ "/" + document.getDocumentName();
            docList.add(message);
        }
        String[] array = docList.toArray(new String[length]);
        return array;
    }*/
}