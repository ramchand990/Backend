package com.healspan.claimservice.mstupload.claim.dao.business;

import com.healspan.claimservice.mstupload.claim.dao.master.GenderMst;
import com.healspan.claimservice.mstupload.claim.dao.master.HospitalMst;
import com.healspan.claimservice.mstupload.claim.dao.master.RoomCategoryMst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patient_info")
public class PatientInfo {

    @Id
    @SequenceGenerator(name="patient_info_generator", sequenceName = "patient_info_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_info_generator")
    @Column(name="id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "date_of_birth")
    private Date dateBirth;
    @Column(name = "age")
    private int age;
    @Column(name = "is_primary_insured")
    private boolean isPrimaryInsured = false;
    @Column(name = "date_of_admission")
    private LocalDateTime dateOfAdmission;
    @Column(name = "estimated_date_of_discharge")
    private LocalDateTime estimatedDateOfDischarge;
    @Column(name = "date_of_discharge")
    private LocalDateTime dateOfDischarge;

    @Column(name = "cost_per_day")
    private double costPerDay;

    @Column(name = "total_room_cost")
    private double totalRoomCost;

    @Column(name = "other_costs_estimation")
    private double otherCostsEstimate;

    @Column(name = "initial_costs_estimation")
    private double initialCostEstimate;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "claimed_amount")
    private double claimedAmount;

    @Column(name = "enhancement_estimation")
    private double enhancementEstimate;

    @Column(name = "final_bill_amount")
    private double finalBillAmount;

    @Column(name = "patient_Uhid")
    private String patientUhid;

    @ManyToOne(fetch = FetchType.LAZY)
    private HospitalMst hospitalMst;

    @ManyToOne(fetch = FetchType.LAZY)
    private RoomCategoryMst roomCategoryMst;
    @ManyToOne(fetch = FetchType.LAZY)
    private GenderMst genderMst;
    @OneToMany(mappedBy = "patientInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ClaimStageLink> claimStageLinkTable;

    @OneToMany(mappedBy = "patientInfo"
            ,cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<PatientAndOtherCostLink> patientAndOtherCostLink;
}
