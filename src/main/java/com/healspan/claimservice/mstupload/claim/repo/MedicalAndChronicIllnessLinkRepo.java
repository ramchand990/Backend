package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.MedicalAndChronicIllnessLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalAndChronicIllnessLinkRepo extends JpaRepository<MedicalAndChronicIllnessLink, Long> {

    @Query(value = "delete from healspan.medical_chronic_illness_link where medical_Info_Id = :medicalInfoId",nativeQuery = true)
    void deleteMedicalInfoId(@Param("medicalInfoId") Long medicalInfoId);
}
