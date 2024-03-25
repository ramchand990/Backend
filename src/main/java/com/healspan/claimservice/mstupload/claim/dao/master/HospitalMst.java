package com.healspan.claimservice.mstupload.claim.dao.master;

import com.healspan.claimservice.mstupload.claim.dao.business.ContactType;
import com.healspan.claimservice.mstupload.claim.dao.business.PatientInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hospital_mst")
public class HospitalMst {

    @Id
    @SequenceGenerator(name="hospital_mst_generator", sequenceName = "hospital_mst_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hospital_mst_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "about")
    private String aboutUs;

    @Column(name = "address")
    private String address;

    @Column(name = "board_line_num")
    private int boardLineNum;

    @Column(name = "gst_num")
    private int gstNum;

    @Column(name = "hospital_id")
    private String hospitalId;

    @Column(name = "hospital_code")
    private String hospitalCode;

    @Column(name = "is_active",columnDefinition = "varchar(1) default 'Y'")
    private String isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    private ContactType contactType;

    @JsonIgnore
    @OneToMany(mappedBy = "hospitalMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<PatientInfo> PatientInfo;
    @JsonIgnore
    @OneToMany(mappedBy = "hospitalMst"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<UserMst> userMst;

    @Override
    public String toString() {
        return "HospitalMst{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hospitalCode='" + hospitalCode + '\'' +
                '}';
    }
}
