package com.healspan.claimservice.mstupload.claim.dao.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "claim_stage_link")
@ToString
public class ClaimStageLink {

    @Id
    @SequenceGenerator(name="claim_stage_link_generator", sequenceName = "claim_stage_link_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_stage_link_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;
    @Column(name = "last_updated_date_time")
    private LocalDateTime lastUpdatedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimInfo claimInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageMst claimStageMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private PatientInfo patientInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private MedicalInfo medicalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private InsuranceInfo insuranceInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private StatusMst statusMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserMst userMst;

    @OneToMany(mappedBy = "claimStageLink"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimAssignment> claimAssignment;
    @JsonIgnore
    @OneToMany(mappedBy = "claimStageLink"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<QuestionAnswer> questionAnswerList;

    @JsonIgnore
    @OneToMany(mappedBy = "claimStageLink"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Document> documentList;

    @JsonIgnore
    @OneToMany(mappedBy = "ClaimStageLink"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<TpaUpdate> TpaUpdate;

    @Override
    public String toString() {
        return "ClaimStageLink{" +
                "id=" + id +
                ", createdDateTime=" + createdDateTime +
                ", claimInfo=" + claimInfo +
                ", claimStageMst=" + claimStageMst +
                ", patientInfo=" + patientInfo +
                ", medicalInfo=" + medicalInfo +
                ", insuranceInfo=" + insuranceInfo +
                ", statusMst=" + statusMst +
                ", userMst=" + userMst +
                '}';
    }
}
