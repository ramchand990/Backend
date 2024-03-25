package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.ClaimStageMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "tpa_update")
public class TpaUpdate {

    @Id
    @SequenceGenerator(name = "tpa_update_generator", sequenceName = "tpa_update_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tpa_update_generator")
    @Column(name = "id")
    private Long id;
    @Column(name = "record_insertion_date")
    private LocalDateTime recordInsertionDate;
    @Column(name = "tpa_claim_number")
    private String tpaClaimNo;
    @Column(name = "approved_amount")
    private double approvedAmount;
    @Column(name = "status")
    private String status;
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimInfo claimInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageMst ClaimStage;
    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageLink ClaimStageLink;

}
