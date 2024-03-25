package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificContent {

    private String iHave;
    private String healspanClaimId;
    private String tpaName;
    private String memberId;
    private String fullName;
    private String mobileNo;
    private String dateOfAdmission;
    private String dateOfDischarge;
    private String doctorName;
    private String doctorRegNumber;
    private String roomType;
    private String proposedLineOfTreatment;
    private String treatment;
    private String diagnosis;
    private String diagnosisMultipleOption;
    private String packageName;
    private String roomRentAndNursingCharges;
    private String consultation;
    private String docList;


    private String Name = "xyz";
    private Long Age = 31L;
    private boolean Status = false;
}
