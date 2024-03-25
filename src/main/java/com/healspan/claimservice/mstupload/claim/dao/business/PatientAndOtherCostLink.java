package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.OtherCostsMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patient_othercost_link")
public class PatientAndOtherCostLink {

    @Id
    @SequenceGenerator(name="patient_othercost_link_generator", sequenceName = "patient_othercost_link_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_othercost_link_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "amount")
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private PatientInfo patientInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private OtherCostsMst otherCostsMst;
}
