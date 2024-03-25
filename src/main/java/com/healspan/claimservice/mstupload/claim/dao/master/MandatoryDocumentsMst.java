package com.healspan.claimservice.mstupload.claim.dao.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mandatory_documents_mst")
public class MandatoryDocumentsMst {

    @Id
    @SequenceGenerator(name="mandatory_documents_mst_generator", sequenceName = "mandatory_documents_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mandatory_documents_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "document_type_mst_id")
    private Long documentTypeId;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "mandatoryDocumentsMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<StageAndDocumentLinkMst> stageAndDocumentLinkMst;

    @Override
    public String toString() {
        return "MandatoryDocumentsMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
