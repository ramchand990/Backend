package com.healspan.claimservice.mstupload.claim.dao.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.business.TpaUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "claim_stage_mst")
public class ClaimStageMst {

    @Id
    @SequenceGenerator(name="claim_stage_mst_generator", sequenceName = "claim_stage_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_stage_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "claimStageMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimStageLink> claimStageLinkTable;

    @JsonIgnore
    @OneToMany(mappedBy = "claimStageMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<StageAndDocumentLinkMst> stageAndDocumentLinkMst;

    @JsonIgnore
    @OneToMany(mappedBy = "claimStageMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimAssignment> claimAssignment;

    @JsonIgnore
    @OneToMany(mappedBy = "claimStage"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<StatusMst> statusMst;

    @JsonIgnore
    @OneToMany(mappedBy = "ClaimStage"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<TpaUpdate> TpaUpdate;


    @Override
    public String toString() {
        return "ClaimStageMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
