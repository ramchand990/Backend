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
@Table(name = "procedure_mst")
public class ProcedureMst {

    @Id
    @SequenceGenerator(name = "procedure_mst_generator", sequenceName = "procedure_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "procedure_mst_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "display_name")
    private String name;
    @Column(name = "rule_engine_name")
    private String ruleEngineName;

    @ManyToOne(fetch = FetchType.LAZY)
    private TpaMst tpaMst;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "procedureMst"
            , cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<MedicalInfo> medicalInfo;

    @Override
    public String toString() {
        return "ProcedureMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
