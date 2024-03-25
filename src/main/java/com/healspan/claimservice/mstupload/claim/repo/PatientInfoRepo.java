package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.PatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientInfoRepo extends JpaRepository<PatientInfo,Long> {
}
