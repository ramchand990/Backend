package com.healspan.claimservice.mstupload.claim.dao.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sla_mst")
public class SlaMst {

    @Id
    @SequenceGenerator(name="sla_mst_generator", sequenceName = "sla_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sla_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "initial_authorization_sla")
    private Long initialAuthorizationSla;

    @Column(name = "enhancement_sla")
    private Long enhancementSla;

    @Column(name = "discharge_sla")
    private Long dischargeSla;

    @Column(name = "finalClaim_sla")
    private Long finalClaimSla;

    @Column(name = "is_active",length = 1)
    private String isActive;
}