package com.healspan.claimservice.mstupload.claim.dao.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "treatment_type_mst")
public class TreatmentTypeMst {

    @Id
    @SequenceGenerator(name="treatment_type_mst_generator", sequenceName = "treatment_type_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "treatment_type_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @Override
    public String toString() {
        return "TreatmentTypeMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
