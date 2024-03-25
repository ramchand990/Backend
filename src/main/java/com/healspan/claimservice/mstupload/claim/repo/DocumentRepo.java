package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepo extends JpaRepository<Document,Long> {

    @Modifying
    @Query(value = "delete from healspan.document where medical_Info_Id = :deleteDocumentId and document_type_mst_id = 2",nativeQuery = true)
    void deleteDocumentId(@Param("deleteDocumentId") Long deleteDocumentId);

    @Modifying
    @Query(value = "delete from healspan.document where claim_Stage_Link_Id = :claimStageLinkId and status = false",nativeQuery = true)
    void deleteByClaimStageLinkId(@Param("claimStageLinkId") Long claimStageLinkId);

    @Query(value = "select * from healspan.document where mandatory_documents_mst_id = :mandatoryDocId and claim_stage_link_id = :claimStageLinkId",nativeQuery = true)
    Optional<Document> findByClaimStageLinkId( @Param("mandatoryDocId") long mandatoryDocId , @Param("claimStageLinkId") long claimStageLinkId);

    List<Document> findAllByClaimStageLinkIdIn(List<Long> claimStageLinkId);
}
