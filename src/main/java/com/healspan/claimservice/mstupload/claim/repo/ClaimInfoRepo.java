package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClaimInfoRepo extends JpaRepository<ClaimInfo,Long> {
    List<ClaimInfo> findAllByUserMstId(long userId);
}
