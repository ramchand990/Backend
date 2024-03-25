package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimStageLinkRepo extends JpaRepository<ClaimStageLink,Long> {
    Optional<ClaimStageLink> findByClaimInfoIdAndClaimStageMstId(Long claimInfoId, Long claimStageMstId);

    List<ClaimStageLink> findAllByClaimInfoId(Long claimInfoId);

    @Query(value = "select id from healspan.claim_stage_link where claim_info_id = :claimId",nativeQuery = true)
    List<Long> findAllByOpenQueryTransaction(@Param("claimId")Long claimId);

    List<ClaimStageLink> findAllByIdIn(List<Long> id);

    @Query(value = "select id from healspan.claim_stage_link where created_date_time in (select max_created_date from (select max(created_date_time) as max_created_date,claim_info_id from healspan.claim_stage_link where user_mst_id = :userId group by claim_info_id) max_date_table)", nativeQuery = true)
    List<Long> findAllByUserMstId(@Param("userId") Long userId);

    @Query(value = "select id from healspan.claim_stage_link where created_date_time in (select max_created_date from (select max(created_date_time) as max_created_date,claim_info_id from healspan.claim_stage_link where claim_info_id in :claimIdList group by claim_info_id) max_date_table)", nativeQuery = true)
    List<Long> findAllByClaimIdList(@Param("claimIdList") List<Long> claimIdList);

    @Query(value = "SELECT MAX(CSL.id) FROM healspan.claim_stage_link CSL inner join healspan.claim_assignment CA on CSL.id = CA.claim_stage_link_id WHERE CA.user_mst_id = :userId GROUP BY CSL.claim_info_id", nativeQuery = true)
    List<Long> findAllByReviewerUserMstId(@Param("userId") Long userId);


    @Query(value = "SELECT MAX(CSL.id) From healspan.claim_stage_link CSL Where CSL.claim_info_id =:claimId", nativeQuery = true)
    Long findByClaimInfoId(@Param("claimId") Long claimId);

    ClaimStageLink findAllById(Long id);
}
