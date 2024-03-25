package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.ChronicIllnessMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "medical_Chronic_Illness_link")
public class MedicalAndChronicIllnessLink {
    @Id
    @SequenceGenerator(name="medical_Chronic_Illness_link_generator", sequenceName = "medical_Chronic_Illness_link_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_Chronic_Illness_link_generator")
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private MedicalInfo medicalInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChronicIllnessMst chronicIllnessMst;
}
