package com.healspan.claimservice.mstupload.claim.dao.master;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "status_mst")
public class StatusMst {

    @Id
    @SequenceGenerator(name="status_mst_generator", sequenceName = "status_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_mst_generator")
    @Column(name="id")
    private Long id;

   @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClaimStageMst claimStage;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserRoleMst userRoleMst;

    @JsonIgnore
    @OneToMany(mappedBy = "statusMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimStageLink> claimStageLinkTable;

    @JsonIgnore
    @OneToMany(mappedBy = "statusMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimAssignment> claimAssignment;

    @Override
    public String toString() {
        return "StatusMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
