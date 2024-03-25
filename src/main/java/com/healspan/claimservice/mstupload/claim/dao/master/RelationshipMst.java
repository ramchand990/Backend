package com.healspan.claimservice.mstupload.claim.dao.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.InsuranceInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "relationship_mst")
public class RelationshipMst {

    @Id
    @SequenceGenerator(name="relationship_mst_generator", sequenceName = "relationship_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "relationship_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "relationshipMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<InsuranceInfo> insuranceInfo;

    @Override
    public String toString() {
        return "RelationshipMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
