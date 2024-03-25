package com.healspan.claimservice.mstupload.claim.dto.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healspan.claimservice.mstupload.claim.dto.master.DiagnosisMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.ProcedureMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.SpecialityMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.TreatmentTypeMstDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MedicalInfoDto {

    private Long id;
    private Long claimInfoId;
    private Long claimStageId;
    private Long claimStageLinkId;
    @JsonProperty
    private boolean isSubmitted;
    private Date dateOfFirstDiagnosis;
    private String doctorName;
    private String doctorRegistrationNumber;
    private String doctorQualification;
    private Long procedureId;
    private ProcedureMstDto procedureMst;
    private Long diagnosisId;
    private DiagnosisMstDto diagnosisMst;
    private Long specialityId;
    private SpecialityMstDto specialityMst;
    private Long treatmentTypeId;
    private TreatmentTypeMstDto treatmentTypeMst;
    private List<MedicalAndChronicIllnessLinkDto> medicalAndChronicIllnessLink;
}