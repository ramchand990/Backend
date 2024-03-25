package com.healspan.claimservice.mstupload.repo;

import com.healspan.claimservice.mstupload.claim.dao.master.ChronicIllnessMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChronicIllnessMstRepo extends JpaRepository<ChronicIllnessMst,Long> {
}
