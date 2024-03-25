package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.*;
import com.healspan.claimservice.mstupload.claim.dto.business.DocumentDto;
import com.healspan.claimservice.mstupload.claim.dto.business.MedicalAndChronicIllnessLinkDto;
import com.healspan.claimservice.mstupload.claim.dto.business.MedicalInfoDto;
import com.healspan.claimservice.mstupload.claim.dto.business.QuestionAnswerDto;
import com.healspan.claimservice.mstupload.claim.repo.DocumentRepo;
import com.healspan.claimservice.mstupload.claim.repo.MedicalAndChronicIllnessLinkRepo;
import com.healspan.claimservice.mstupload.claim.repo.MedicalInfoRepo;
import com.healspan.claimservice.mstupload.claim.repo.QuestionAnswerRepo;
import com.healspan.claimservice.mstupload.repo.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MedicalInfoUtil {

    private Logger logger = LoggerFactory.getLogger(MedicalInfoUtil.class);

    @Autowired
    private DiagnosisMstRepo diagnosisMstRepo;
    @Autowired
    private ProcedureMstRepo procedureMstRepo;
    @Autowired
    private SpecialityMstRepo specialityMstRepo;
    @Autowired
    private MedicalInfoRepo medicalInfoRepo;
    @Autowired
    private TreatmentTypeMstRepo treatmentTypeMstRepo;
    @Autowired
    private MedicalAndChronicIllnessLinkRepo medicalAndChronicIllnessLinkRepo;

    @Autowired
    private QuestionAnswerRepo questionAnswerRepo;

    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ChronicIllnessMstRepo chronicIllnessMstRepo;
    @Autowired
    private MandatoryDocumentsMstRepo mandatoryDocumentsMstRepo;

    public MedicalInfo saveOrUpdateMedicalInfo(MedicalInfoDto dto){
        logger.debug("MedicalInfoUtil::saveOrUpdatePatientInfo requestedData: {} --Start",dto);
        MedicalInfo medicalInfoDao = modelMapper.map(dto, MedicalInfo.class);
        if(dto.getId()!=null && dto.getClaimStageLinkId()!=null) {
            medicalInfoDao.setId(dto.getId());
        }
        logger.debug("Find the Diagnosis record from db for diagnosisId:{} and set to medicalInfoDao",dto.getDiagnosisId());
        medicalInfoDao.setDiagnosisMst(diagnosisMstRepo.findById(dto.getDiagnosisId()).get());
        logger.debug("Find the Procedure record from db for procedureId:{} and set to medicalInfoDao",dto.getProcedureId());
        medicalInfoDao.setProcedureMst(procedureMstRepo.findById(dto.getProcedureId()).get());
        logger.debug("Find the Speciality record from db for specialityId:{} and set to medicalInfoDao",dto.getSpecialityId());
        medicalInfoDao.setSpecialityMst(specialityMstRepo.findById(dto.getSpecialityId()).get());
        logger.debug("Find the treatmentType record from db for treatmentTypId:{} and set to medicalInfoDao",dto.getTreatmentTypeId());
        medicalInfoDao.setTreatmentTypeMst(treatmentTypeMstRepo.findById(dto.getTreatmentTypeId()).get());
        logger.debug("Save updated Medical record in db");
        medicalInfoRepo.save(medicalInfoDao);
        logger.debug("Successfully saved updated Medical record in db");
        deleteChronicIllnessDetail(medicalInfoDao);
        saveChronicIllnessDetail(dto.getMedicalAndChronicIllnessLink(), medicalInfoDao);
        logger.debug("MedicalInfoUtil::saveOrUpdatePatientInfo responseData: {} --End",medicalInfoDao);
        return medicalInfoDao;
    }

    private void deleteChronicIllnessDetail(MedicalInfo medicalInfoDao){
        logger.debug("MedicalInfoUtil::deleteChronicIllnessDetail requestedData: {} --Start",medicalInfoDao);
        try {
            logger.debug("Delete  medicalAndChronicIllness record from db for medicalId:{}",medicalInfoDao.getId());
            medicalAndChronicIllnessLinkRepo.deleteMedicalInfoId(medicalInfoDao.getId());
        }catch (Exception e){
            logger.error("Exception Details: \n {}", ExceptionUtils.getStackTrace(e));
        }
        logger.debug("MedicalInfoUtil::deleteChronicIllnessDetail dataDeleted: {} --End",medicalInfoDao);
    }

    @Transactional
    public void saveChronicIllnessDetail(List<MedicalAndChronicIllnessLinkDto> dto, MedicalInfo medicalInfoDao) {
        logger.debug("MedicalInfoUtil :: deleteChronicIllnessDetail requestedDto: {} and medicalInfoDao: {} --Start",dto,medicalInfoDao);
        List<MedicalAndChronicIllnessLink> linkDaoList = new ArrayList<>();
        if (dto != null && !dto.isEmpty()) {
            for (MedicalAndChronicIllnessLinkDto detail : dto) {
                MedicalAndChronicIllnessLink link = new MedicalAndChronicIllnessLink();
                logger.debug("Find chronicIllness record from db for chronicIllnessId:{} and set to MedicalAndChronicIllnessLink",detail.getId());
                link.setChronicIllnessMst(chronicIllnessMstRepo.findById(detail.getId()).get());
                link.setMedicalInfo(medicalInfoDao);
                linkDaoList.add(link);
            }
            if (!linkDaoList.isEmpty()){
                logger.info("Save all medicalAndChronicIllness records in db");
                medicalAndChronicIllnessLinkRepo.saveAll(linkDaoList);
            }
            logger.debug("MedicalInfoUtil::saveChronicIllnessDetail savedData: {}",linkDaoList);
        }
        logger.debug("MedicalInfoUtil :: deleteChronicIllnessDetail  SaveChronicIllnessDetail: {} --End",linkDaoList);
    }

    public void saveQuestionnairesData(List<QuestionAnswerDto> sequentialQuestion, ClaimStageLink claimStageLinkDao){
        logger.debug("MedicalInfoUtil::saveQuestionnairesData requestedSequentialQuestion: {} and claimStageLinkDao: {} --Start",sequentialQuestion,claimStageLinkDao);
        List<QuestionAnswer> questionAnswerDaoList = new ArrayList<>();
        if(sequentialQuestion!=null && !sequentialQuestion.isEmpty()){
            logger.info("QuestionnairesUtil : saveQuestionnairesData :  --Start");
            sequentialQuestion.stream().forEach(obj->{
                QuestionAnswer questionAnswerDao = new QuestionAnswer();
                questionAnswerDao.setQuestion(obj.getQuestion());
                questionAnswerDao.setAnswer(obj.getAnswer());
                questionAnswerDao.setClaimStageLink(claimStageLinkDao);
                questionAnswerDaoList.add(questionAnswerDao);
            });
            if(!questionAnswerDaoList.isEmpty()) {
                logger.info("Save all questionAnswer records in db");
                questionAnswerRepo.saveAll(questionAnswerDaoList);
                logger.info("QuestionnairesUtil : saveQuestionnairesData : --End");
            }
        }
        logger.debug("MedicalInfoUtil::saveQuestionnairesData savedData: {} --End",questionAnswerDaoList);
    }


    public void saveDocumentData(List<DocumentDto> documentList, ClaimStageLink claimStageLinkDao){
        logger.debug("MedicalInfoUtil::saveDocumentData requestedDocumentList: {} and claimStageLinkDao: {} --Start",documentList,claimStageLinkDao);
        List<Document> documentDaoList = new ArrayList<>();
        if(documentList!=null && !documentList.isEmpty()){
            logger.info("DocumentUtil :: saveDocumentData :: --Start");
            documentList.stream().forEach(obj->{
                Document document = new Document();
                document.setDocumentName(obj.getDocumentName());
                document.setDocumentPath(obj.getDocumentPath());
                document.setClaimStageLink(claimStageLinkDao);
               /* document.setMandatoryDocumentsMst(mandatoryDocumentsMstRepo.findById(obj.getDocumentTypeId()).get());*/
                documentDaoList.add(document);
            });
            if(!documentDaoList.isEmpty())
                logger.info("Save all questionAnswer records in db");
                documentRepo.saveAll(documentDaoList);
        }
        logger.debug("MedicalInfoUtil::saveDocumentData savedData: {} --End",documentDaoList);
    }
    public ClaimStageLink copyExistingMedicalInfo(ClaimStageLink existingClaimStageLinkDao, ClaimStageLink newClaimStageLinkDao){
        logger.info("MedicalInfo::copyExistingMedicalInfo requestedExistingClaimStageLinkDao: {} and newClaimStageLinkDao:{} --Start",existingClaimStageLinkDao,newClaimStageLinkDao);
        MedicalInfo existingMedicalInfo = existingClaimStageLinkDao.getMedicalInfo();
        MedicalInfoDto medicalInfoDto = modelMapper.map(existingMedicalInfo, MedicalInfoDto.class);
       /* medicalInfoDto.setId(null);*/
        MedicalInfo MedicalInfo = modelMapper.map(medicalInfoDto,MedicalInfo.class);
        logger.debug("Save Medical record in db");

        medicalInfoRepo.save(MedicalInfo);

        /*if(!existingMedicalInfo.getMedicalAndChronicIllnessLink().isEmpty()){

            List<MedicalAndChronicIllnessLink>  newMedicalAndChronicIllnessLinkDaoList = new ArrayList<>();
            for(MedicalAndChronicIllnessLink medicalAndChronicIllnessLinkDao : existingMedicalInfo.getMedicalAndChronicIllnessLink()){
                MedicalAndChronicIllnessLinkDto medicalAndChronicIllnessLinkDto = modelMapper.map(medicalAndChronicIllnessLinkDao, MedicalAndChronicIllnessLinkDto.class);
                medicalAndChronicIllnessLinkDto.setId(null);
                MedicalAndChronicIllnessLink medicalAndChronicIllnessLink = modelMapper.map(medicalAndChronicIllnessLinkDto,MedicalAndChronicIllnessLink.class);
                medicalAndChronicIllnessLink.setMedicalInfo(newMedicalInfo);
                newMedicalAndChronicIllnessLinkDaoList.add(medicalAndChronicIllnessLink);
            }
            logger.info("Save all medicalAndChronicIllness records in db");
            medicalAndChronicIllnessLinkRepo.saveAll(newMedicalAndChronicIllnessLinkDaoList);
        }*/
        newClaimStageLinkDao.setMedicalInfo(MedicalInfo);
        logger.info("MedicalInfo::copyExistingMedicalInfo responseData: {} --End",newClaimStageLinkDao);
        return newClaimStageLinkDao;
    }
}