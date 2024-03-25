package com.healspan.claimservice.mstupload.claim.dto.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healspan.claimservice.mstupload.claim.dto.master.GenderMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.HospitalMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.RoomCategoryMstDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PatientInfoDto {

    private Long id;
    private Long claimStageLinkId;
    @JsonProperty
    private boolean isSubmitted;
    private String firstName;
    private String middleName;
    private String lastname;
    private String mobileNo;
    private Date dateBirth;
    private int age;
    @JsonProperty
    private boolean isPrimaryInsured;
    private LocalDateTime dateOfAdmission;
    private LocalDateTime estimatedDateOfDischarge;
    private LocalDateTime dateOfDischarge;
    private double costPerDay;
    private double totalRoomCost;
    private double otherCostsEstimate;
    private double initialCostEstimate;
    private String billNumber;
    private double claimedAmount;
    private double enhancementEstimate;
    private double finalBillAmount;
    private String patientUhid;
    private Long hospitalId;
    private HospitalMstDto hospitalMst;
    private List<PatientAndOtherCostLinkDto> patientAndOtherCostLink;
    private Long roomCategoryId;
    private RoomCategoryMstDto roomCategoryMst;
    private Long genderId;
    private GenderMstDto genderMst;
}
