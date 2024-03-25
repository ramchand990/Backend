package com.healspan.claimservice.mstupload.claim.dto.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReportDto {

    private String name;
    private String age;
    private String gender;
    private String dob;
    private String insuranceCompany;
    private String tpa;
    private String tpaIdCardNo;
    private String relation;
    private String policyHolder;
    private String procedure;
    private String treatmentType;
    private String speciality;
    private String dateOfFirstDiagnosis;
    private String doctorName;
    private String doctorRegistrationNo;
    private String doctorQualification;
    private List<String> initDocumentName;
    private List<String> enhDocumentName;
    private List<String> disDocumentName;
    private List<String> fnlDocumentName;
    private String totalRoomCost;

    private double initApprovalAmount = 0.00;
    private double initCostEstimate = 0.00;

}



