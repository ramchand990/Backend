package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.MandatoryDocumentsMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "document")
public class Document {

    @Id
    @SequenceGenerator(name = "document_generator", sequenceName = "document_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String documentName;

    @Column(name = "path")
    private String documentPath;

    @Column(name ="status")
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageLink claimStageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    private MandatoryDocumentsMst mandatoryDocumentsMst;
}