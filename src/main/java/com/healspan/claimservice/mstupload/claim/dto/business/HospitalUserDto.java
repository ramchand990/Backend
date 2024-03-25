package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HospitalUserDto {

    private Long claimId;
    private String patientName;
    private String ailement;
    private String stage;
    private String status;
    private Date admissionDate;
    private Date dischargeDate;
    private String ageing;
    private double approvedAmount;
}
