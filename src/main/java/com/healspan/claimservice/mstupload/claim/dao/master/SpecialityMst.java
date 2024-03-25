package com.healspan.claimservice.mstupload.claim.dao.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.MedicalInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "speciality_mst")
public class SpecialityMst {

    @Id
    @SequenceGenerator(name="speciality_mst_generator", sequenceName = "speciality_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "speciality_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "specialityMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<MedicalInfo> medicalInfo;

    @Override
    public String toString() {
        return "SpecialityMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
