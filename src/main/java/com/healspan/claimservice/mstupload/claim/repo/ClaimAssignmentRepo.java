package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimAssignment;
import com.healspan.claimservice.mstupload.claim.dto.business.AssignCommentDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface ClaimAssignmentRepo extends JpaRepository<ClaimAssignment, Long> {
    List<ClaimAssignment> findAllByClaimStageLinkId(Long claimStageLinkId);

    List<ClaimAssignment> findAllByClaimInfoIdIn(List<Long> claimIdList);

    ClaimAssignment findFirst1ByUserMstIdInOrderByAssignedDateTimeDesc(List<Long> userIdList);
    List<ClaimAssignment> findAllByClaimInfoIdAndClaimStageMstIdOrderByAssignedDateTimeDesc(long claimInfoId,long claimStageMstId);

    List<ClaimAssignment> findAllByClaimInfoIdOrderByAssignedDateTimeDesc(long claimInfoId);

    @Query(value = "select * from healspan.claim_assignment where claim_info_id = :claimInfoId and claim_stage_mst_id = :claimStageMstId and completed_date_time IS NULL", nativeQuery = true)
    ClaimAssignment findAllByOpenQueryTransaction(@Param("claimInfoId") Long claimInfoId, @Param("claimStageMstId") long claimStageMstId);

    @Modifying
    @Query(value = "update healspan.claim_assignment set completed_date_time=current_timestamp where claim_info_id = :claimInfoId and claim_stage_mst_id = :claimStageMstId and completed_date_time IS NULL", nativeQuery = true)
    void updateClosedDateTime(@Param("claimInfoId") long claimInfoId, @Param("claimStageMstId") long claimStageMstId);

    @Query(value = "select * from healspan.claim_assignment where claim_info_id = :claimInfoId and completed_date_time IS NULL", nativeQuery = true)
    Optional<ClaimAssignment> findByClaimInfoId(@Param("claimInfoId") Long claimInfoId);

    @Modifying
    @Query(value = "select * from healspan.claim_assignment\n" +
            "\twhere claim_info_id = :claimInfoId and assigned_comments is not null order by assigned_date_time desc", nativeQuery = true)
    List<ClaimAssignment> getAllAssigneeComment(@Param("claimInfoId") Long claimInfoId);
}
