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
@Table(name = "tpa_mst")
public class TpaMst {

    @Id
    @SequenceGenerator(name="tpa_mst_generator", sequenceName = "tpa_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tpa_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active",length = 1)
    private String isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "tpaMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<InsuranceInfo> insuranceInfo;

    @JsonIgnore
    @OneToMany(mappedBy = "tpaMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<DiagnosisMst> diagnosisMst;

    @JsonIgnore
    @OneToMany(mappedBy = "tpaMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ProcedureMst> procedureMst;

    @JsonIgnore
    @OneToMany(mappedBy = "tpaMst"
            , cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<RoomCategoryMst> roomCategoryMst;

    @Override
    public String toString() {
        return "TpaMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
