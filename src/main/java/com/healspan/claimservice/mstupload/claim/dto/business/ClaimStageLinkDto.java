package com.healspan.claimservice.mstupload.claim.dto.business;

import com.healspan.claimservice.mstupload.claim.dto.master.ClaimStageMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.StatusMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.UserMstDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClaimStageLinkDto {

    private Long id;
    private ClaimInfoDto claimInfo;
    private ClaimStageMstDto claimStageMst;
    private PatientInfoDto patientInfo;
    private MedicalInfoDto medicalInfo;
    private InsuranceInfoDto insuranceInfo;
    private List<QuestionAnswerDto> questionAnswerList;
    private List<DocumentDto> documentList;
    private StatusMstDto statusMst;
    private UserMstDto userMst;
    private ClaimAssignmentDto claimAssignment;
}
