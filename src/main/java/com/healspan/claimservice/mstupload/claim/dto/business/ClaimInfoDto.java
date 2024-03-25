package com.healspan.claimservice.mstupload.claim.dto.business;

import com.healspan.claimservice.mstupload.claim.dto.master.ClaimStageMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.HospitalMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.StatusMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.UserMstDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClaimInfoDto {

    private Long id;
    private boolean isSubmitted;
    private String tpaClaimNumber;
    private LocalDateTime createdDateTime;

    private Long claimStageId;
    private ClaimStageMstDto claimStageMst;
    private Long claimStageLinkId;

    private Long userId;
    private UserMstDto userMst;

    private Long hospitalId;
    private HospitalMstDto hospitalMst;
    private Long statusId;
    private StatusMstDto statusMstDto;
    private PatientInfoDto patientInfoDto;
    private MedicalInfoDto medicalInfoDto;
    private InsuranceInfoDto insuranceInfoDto;
    private Long assignId;
    private String requestType;

}
