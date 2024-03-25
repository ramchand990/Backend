package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.DiagnosisMst;
import com.healspan.claimservice.mstupload.claim.dao.master.ProcedureMst;
import com.healspan.claimservice.mstupload.claim.dao.master.SpecialityMst;
import com.healspan.claimservice.mstupload.claim.dao.master.TreatmentTypeMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "medical_info")
public class MedicalInfo {

    @Id
    @SequenceGenerator(name="medical_info_generator", sequenceName = "medical_info_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_info_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "date_of_first_diagnosis")
    private Date dateOfFirstDiagnosis;
    @Column(name = "doctor_name")
    private String doctorName;
    @Column(name = "doctor_registration_number")
    private String doctorRegistrationNumber;
    @Column(name = "doctor_qualification")
    private String doctorQualification;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProcedureMst procedureMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private DiagnosisMst diagnosisMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private SpecialityMst specialityMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private TreatmentTypeMst treatmentTypeMst;

    @OneToMany(mappedBy = "medicalInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimStageLink> claimStageLinkTable;

    @OneToMany(mappedBy = "medicalInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<MedicalAndChronicIllnessLink> medicalAndChronicIllnessLink;
}
