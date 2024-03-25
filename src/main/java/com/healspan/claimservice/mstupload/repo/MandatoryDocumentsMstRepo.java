package com.healspan.claimservice.mstupload.repo;

import com.healspan.claimservice.mstupload.claim.dao.master.MandatoryDocumentsMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MandatoryDocumentsMstRepo extends JpaRepository<MandatoryDocumentsMst, Long> {
    MandatoryDocumentsMst findByName(String name);
}
