package com.healspan.claimservice.report;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dto.business.*;
import com.healspan.claimservice.mstupload.claim.dto.master.*;
import com.healspan.claimservice.mstupload.claim.dto.report.ReportDto;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReportUtil {
    private final Logger logger = LoggerFactory.getLogger(ReportUtil.class);
    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;
    @Autowired
    private ModelMapper modelMapper;


    public List<ReportDto> reportData(Long claimInfoId) {
        logger.debug("ReportUtil::reportData Request received for patient data in report for claimInfoId: {} --Start", claimInfoId);
        List<ReportDto> reportData = null;
        if (claimInfoId != null) {
            logger.debug("Find claimStageLink record in db for claimId: {}", claimInfoId);
            Long id = claimStageLinkRepo.findByClaimInfoId(claimInfoId);
            if (id != null) {
                logger.debug("Find claimStageLink record in db for claimStageLinkId: {}", id);
                Optional<ClaimStageLink> claimStageLinkDao = claimStageLinkRepo.findById(id);
                if (claimStageLinkDao.isPresent()) {
                    reportData = new ArrayList<>();
                    ClaimStageLinkDto claimStageLinkDto = modelMapper.map(claimStageLinkDao.get(), ClaimStageLinkDto.class);
                    PatientInfoDto patientInfo = claimStageLinkDto.getPatientInfo();
                    MedicalInfoDto medicalInfoDto = claimStageLinkDto.getMedicalInfo();
                    ProcedureMstDto procedureMstDto = medicalInfoDto.getProcedureMst();
                    ClaimStageMstDto claimStageMstDto = claimStageLinkDto.getClaimStageMst();
                    TreatmentTypeMstDto treatmentTypeMstDto = medicalInfoDto.getTreatmentTypeMst();
                    SpecialityMstDto specialityMstDto = medicalInfoDto.getSpecialityMst();
                    InsuranceInfoDto insuranceInfoDto = claimStageLinkDto.getInsuranceInfo();
                    InsuranceCompanyMstDto insuranceCompanyMstDto = insuranceInfoDto.getInsuranceCompanyMst();
                    TpaMstDto tpaMstDto = insuranceInfoDto.getTpaMst();
                    RelationshipMstDto relationshipMstDto = insuranceInfoDto.getRelationshipMst();
                    List<DocumentDto> documentDtos = claimStageLinkDto.getDocumentList();
                    ReportDto reportDto = new ReportDto();
                    reportDto.setName(patientInfo.getFirstName() + " " + patientInfo.getMiddleName() + " " + patientInfo.getLastname());
                    reportDto.setAge(String.valueOf(patientInfo.getAge()));
                    reportDto.setGender(patientInfo.getGenderMst().getName());
                    reportDto.setDob(String.valueOf(patientInfo.getDateBirth()));
                    reportDto.setProcedure(procedureMstDto.getName());
                    reportDto.setTreatmentType(treatmentTypeMstDto.getName());
                    reportDto.setSpeciality(specialityMstDto.getName());
                    reportDto.setDateOfFirstDiagnosis(String.valueOf(medicalInfoDto.getDateOfFirstDiagnosis()));
                    reportDto.setDoctorName(medicalInfoDto.getDoctorName());
                    reportDto.setDoctorRegistrationNo(medicalInfoDto.getDoctorRegistrationNumber());
                    reportDto.setDoctorQualification(medicalInfoDto.getDoctorQualification());
                    reportDto.setInsuranceCompany(insuranceCompanyMstDto.getName());
                    reportDto.setTpa(tpaMstDto.getName());
                    reportDto.setTpaIdCardNo(insuranceInfoDto.getTpaNumber());
                    reportDto.setRelation(relationshipMstDto.getName());
                    reportDto.setPolicyHolder(insuranceInfoDto.getPolicyHolderName());
                    reportDto.setInitCostEstimate(patientInfo.getInitialCostEstimate());
                    reportDto.setInitApprovalAmount(patientInfo.getFinalBillAmount());
                    if (claimStageMstDto.getClaimStageMstId().equals(1L)) {
                        reportDto.setInitDocumentName(documentDtos.stream().map(obj -> obj.getMandatoryDocumentName()).collect(Collectors.toList()));

                    } else if (claimStageMstDto.getClaimStageMstId().equals(2L)) {
                        reportDto.setEnhDocumentName(documentDtos.stream().map(obj -> obj.getMandatoryDocumentName()).collect(Collectors.toList()));
                    } else if (claimStageMstDto.getClaimStageMstId().equals(3L)) {
                        reportDto.setDisDocumentName(documentDtos.stream().map(obj -> obj.getMandatoryDocumentName()).collect(Collectors.toList()));
                    } else {
                        reportDto.setFnlDocumentName(documentDtos.stream().map(obj -> obj.getMandatoryDocumentName()).collect(Collectors.toList()));
                    }
                    reportData.add(reportDto);
                }
            }
        }
        logger.debug("ReportUtil::reportData responseData: {} --End", reportData);
        return reportData;
    }
}
