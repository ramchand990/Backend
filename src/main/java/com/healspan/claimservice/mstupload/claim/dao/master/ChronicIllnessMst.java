package com.healspan.claimservice.mstupload.claim.dao.master;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healspan.claimservice.mstupload.claim.dao.business.MedicalAndChronicIllnessLink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chronic_illness_mst")
public class ChronicIllnessMst {

    @Id
    @SequenceGenerator(name="chronic_illness_mst_generator", sequenceName = "chronic_illness_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chronic_illness_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "chronicIllnessMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<MedicalAndChronicIllnessLink> medicalAndChronicIllnessLink;

    @Override
    public String toString() {
        return "ChronicIllnessMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
