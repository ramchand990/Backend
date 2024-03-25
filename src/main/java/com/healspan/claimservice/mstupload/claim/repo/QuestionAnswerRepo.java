package com.healspan.claimservice.mstupload.claim.repo;

import com.healspan.claimservice.mstupload.claim.dao.business.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerRepo extends JpaRepository<QuestionAnswer, Long> {

    @Modifying
    @Query(value = "delete from healspan.question_answer where claim_stage_link_id = :questionAnswerId", nativeQuery = true)
    void deleteQuestionAnswerId(@Param("questionAnswerId") Long questionAnswerId);
}
