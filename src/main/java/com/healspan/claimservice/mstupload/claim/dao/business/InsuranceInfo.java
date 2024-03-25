package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.InsuranceCompanyMst;
import com.healspan.claimservice.mstupload.claim.dao.master.RelationshipMst;
import com.healspan.claimservice.mstupload.claim.dao.master.TpaMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "insurance_info")
public class InsuranceInfo {

    @Id
    @SequenceGenerator(name="insurance_info_generator", sequenceName = "insurance_info_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insurance_info_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "tpa_number")
    private String tpaNumber;
    @Column(name = "policy_holder_name")
    private String policyHolderName;
    @Column(name = "policy_number")
    private String policyNumber;
    @Column(name = "is_group_policy")
    private boolean isGroupPolicy;
    @Column(name = "group_company")
    private String groupCompany;
    @Column(name = "group_company_emp_id")
    private String groupCompanyEmpId;
    @Column(name = "claim_id_pre_auth_number")
    private String claimIDPreAuthNumber;
    @Column(name = "initial_approval_limit")
    private double initialApprovalAmount;
    @Column(name = "approval_enhancement_amount")
    private double approvedEnhancementsAmount;
    @Column(name = "approval_amount_at_discharge")
    private double approvedAmountAtDischarge;

    @ManyToOne(fetch = FetchType.LAZY)
    private InsuranceCompanyMst insuranceCompanyMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private TpaMst tpaMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private RelationshipMst relationshipMst;

    @OneToMany(mappedBy = "insuranceInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimStageLink> claimStageLinkTable;

    @Override
    public String toString() {
        return "InsuranceInfo{" +
                "id=" + id +
                ", tpaNumber='" + tpaNumber + '\'' +
                ", policyHolderName='" + policyHolderName + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", isGroupPolicy=" + isGroupPolicy +
                ", groupCompany='" + groupCompany + '\'' +
                ", claimIDPreAuthNumber='" + claimIDPreAuthNumber + '\'' +
                ", initialApprovalAmount=" + initialApprovalAmount +
                ", approvedEnhancementsAmount=" + approvedEnhancementsAmount +
                ", approvedAmountAtDischarge=" + approvedAmountAtDischarge +
                '}';
    }
}
