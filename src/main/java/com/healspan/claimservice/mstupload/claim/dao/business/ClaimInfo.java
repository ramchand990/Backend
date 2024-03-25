package com.healspan.claimservice.mstupload.claim.dao.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.master.HospitalMst;
import com.healspan.claimservice.mstupload.claim.dao.master.UserMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "claim_info")
public class ClaimInfo {

    @Id
    @SequenceGenerator(name="claim_info_generator", sequenceName = "claim_info_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_info_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @Column(name = "tpa_claim_number")
    private String tpaClaimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserMst userMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private HospitalMst hospitalMst;

    @OneToMany(mappedBy = "claimInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimStageLink> claimStageLinkTable;

    @OneToMany(mappedBy = "claimInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimAssignment> claimAssignment;

    @JsonIgnore
    @OneToMany(mappedBy = "claimInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<TpaUpdate> TpaUpdate;
}
