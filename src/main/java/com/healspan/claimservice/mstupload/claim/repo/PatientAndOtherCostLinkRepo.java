package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.PatientAndOtherCostLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientAndOtherCostLinkRepo extends JpaRepository<PatientAndOtherCostLink,Long> {

    @Query(value = "delete from healspan.patient_othercost_link where patient_Info_Id = :patientInfoId",nativeQuery = true)
    void deleteByPatientInfoId(@Param("patientInfoId") Long patientInfoId);
}
