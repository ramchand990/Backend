package com.healspan.claimservice.mstupload.claim.dao.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stage_and_document_link_mst")
public class StageAndDocumentLinkMst {

    @Id
    @SequenceGenerator(name="stage_and_document_link_mst_generator", sequenceName = "stage_and_document_link_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stage_and_document_link_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageMst claimStageMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private MandatoryDocumentsMst mandatoryDocumentsMst;
}
