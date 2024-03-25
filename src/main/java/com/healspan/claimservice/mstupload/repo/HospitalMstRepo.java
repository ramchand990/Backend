package com.healspan.claimservice.mstupload.repo;

import com.healspan.claimservice.mstupload.claim.dao.master.HospitalMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalMstRepo extends JpaRepository<HospitalMst,Long> {
    List<HospitalMst> findByName(String name);
}
