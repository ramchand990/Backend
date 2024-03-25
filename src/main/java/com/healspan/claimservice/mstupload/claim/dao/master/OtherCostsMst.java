package com.healspan.claimservice.mstupload.claim.dao.master;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.PatientAndOtherCostLink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "other_costs_mst")
public class OtherCostsMst {

    @Id
    @SequenceGenerator(name="other_costs_mst_generator", sequenceName = "other_costs_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "other_costs_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "otherCostsMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<PatientAndOtherCostLink> patientAndOtherCostLink;

    @Override
    public String toString() {
        return "OtherCostsMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
