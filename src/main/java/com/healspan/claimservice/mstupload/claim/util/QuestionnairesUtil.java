package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.business.QuestionAnswer;
import com.healspan.claimservice.mstupload.claim.dto.business.QuestionAnswerDto;
import com.healspan.claimservice.mstupload.claim.repo.QuestionAnswerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionnairesUtil {

    private Logger logger = LoggerFactory.getLogger(QuestionnairesUtil.class);

    @Autowired
    private QuestionAnswerRepo questionAnswerRepo;
    @Transactional
    public void saveQuestionnairesData(List<QuestionAnswerDto> sequentialQuestion, ClaimStageLink claimStageLinkDao){
        logger.debug("QuestionnairesUtil::saveQuestionnairesData --Start");
        List<QuestionAnswer> questionAnswerDaoList = new ArrayList<>();
        if(sequentialQuestion!=null && !sequentialQuestion.isEmpty()){
            sequentialQuestion.stream().forEach(obj->{
                QuestionAnswer questionAnswerDao = new QuestionAnswer();
                questionAnswerDao.setQuestion(obj.getQuestion());
                questionAnswerDao.setAnswer(obj.getAnswer());
                questionAnswerDao.setClaimStageLink(claimStageLinkDao);
                questionAnswerDaoList.add(questionAnswerDao);
            });
            logger.debug("Delete questionAnswer record from db for questionAnswerId:{}",claimStageLinkDao.getId());
            questionAnswerRepo.deleteQuestionAnswerId(claimStageLinkDao.getId());
            if(!questionAnswerDaoList.isEmpty()) {
                logger.debug("Save all questionAnswer records in db");
                questionAnswerRepo.saveAll(questionAnswerDaoList);
            }
        }
        logger.debug("QuestionnairesUtil::saveQuestionnairesData --End");
    }
}
