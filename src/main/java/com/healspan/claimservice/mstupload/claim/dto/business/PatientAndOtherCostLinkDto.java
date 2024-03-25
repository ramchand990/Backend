package com.healspan.claimservice.mstupload.claim.dto.business;

import com.healspan.claimservice.mstupload.claim.dto.master.OtherCostsMstDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PatientAndOtherCostLinkDto {
    private Long id;
    private double amount;
    /*private PatientInfoDto patientInfo;*/
    private OtherCostsMstDto otherCostsMst;
}