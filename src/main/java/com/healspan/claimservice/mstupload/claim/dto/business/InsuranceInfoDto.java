package com.healspan.claimservice.mstupload.claim.dto.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healspan.claimservice.mstupload.claim.dto.master.InsuranceCompanyMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.RelationshipMstDto;
import com.healspan.claimservice.mstupload.claim.dto.master.TpaMstDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InsuranceInfoDto {

    private Long id;
    private Long claimInfoId;
    private Long claimStageId;
    private Long claimStageLinkId;
    @JsonProperty
    private boolean isSubmitted;
    private String tpaNumber;
    private String policyHolderName;
    private String policyNumber;
    @JsonProperty
    private boolean isGroupPolicy;
    private String groupCompany;
    private String groupCompanyEmpId;
    private String claimIDPreAuthNumber;
    private double initialApprovalAmount;
    private double approvedEnhancementsAmount;
    private double approvedAmountAtDischarge;
    private Long insuranceCompanyId;
    private InsuranceCompanyMstDto insuranceCompanyMst;
    private Long tpaId;
    private TpaMstDto tpaMst;
    private Long relationshipId;
    private RelationshipMstDto relationshipMst;
}
