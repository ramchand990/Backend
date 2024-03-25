package com.healspan.claimservice.mstupload.claim.util;

import com.healspan.claimservice.mstupload.claim.dao.business.ClaimStageLink;
import com.healspan.claimservice.mstupload.claim.dao.business.Document;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import com.healspan.claimservice.mstupload.claim.repo.DocumentRepo;
import com.healspan.claimservice.mstupload.claim.repo.MedicalInfoRepo;
import com.healspan.claimservice.mstupload.repo.MandatoryDocumentsMstRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class DocumentUtil {

    private Logger logger = LoggerFactory.getLogger(DocumentUtil.class);
    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private MedicalInfoRepo medicalInfoRepo;

    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;
    @Autowired
    private MandatoryDocumentsMstRepo mandatoryDocumentsMstRepo;

    @Transactional
    public Map<Long,Long> saveDocumentData(List<Long> documentIdList, ClaimStageLink claimStageLinkDao){
        logger.debug("DocumentUtil::saveDocumentData requestedDocumentIdList: {} and claimStageLinkDao: {} --Start",documentIdList,claimStageLinkDao);
        Map<Long,Long> documentMap = new HashMap<>();
        List<Document> documentDaoList = new ArrayList<>();
        if(documentIdList!=null && !documentIdList.isEmpty()){
            documentIdList.stream().forEach(obj->{
                Document document = new Document();
                document.setMandatoryDocumentsMst(mandatoryDocumentsMstRepo.findById(obj).get());
                document.setClaimStageLink(claimStageLinkDao);
                documentDaoList.add(document);
            });
            /*documentRepo.deleteByClaimStageLinkId(claimStageLinkDao.getId());*/
        if(!documentDaoList.isEmpty()) {
            logger.debug("Save all Document records in db ");
            documentRepo.saveAll(documentDaoList);
            logger.debug("Successfully saved all Document records in db ");
        }
    }
        if(!documentDaoList.isEmpty())
                documentDaoList.forEach(obj->documentMap.put(obj.getMandatoryDocumentsMst().getId(),obj.getId()));
        logger.debug("DocumentUtil::saveDocumentData  responseData: {} --End",documentMap);
        return documentMap;
    }
    public Document getDocumentDetails(Long documentId){
        logger.info("DocumentUtil::getDocumentDetails requestedData: {} --Start",documentId);
        Optional<Document> document = documentRepo.findById(documentId);
        logger.info("DocumentUtil::getDocumentDetails responseDat: {} --End",document);
       return document.get();
    }
    public Document updateDocumentDetails(Long id, String fileName, String filePath){
        logger.debug("DocumentUtil::updateDocumentDetails requestedId: {},fileName: {} and filePath: {} --Start",id,fileName,filePath);
        Document document =null;
        Optional<Document> optional = documentRepo.findById(id);

        if (optional.isPresent()){
            document = optional.get();
            document.setDocumentName(fileName);
            document.setDocumentPath(filePath);
            document.setStatus(true);
            document = documentRepo.save(document);
        }
        logger.debug("DocumentUtil::updateDocumentDetails responseData: {} --End",document);
        return document;
    }
    public ClaimStageLink getClaimStageLinkId(Long claimStageLinkId) {
        logger.debug("DocumentUtil::updateDocumentDetails requestedClaimStageLinkId: {} --Start",claimStageLinkId);
        Optional<ClaimStageLink> claimStageLink = claimStageLinkRepo.findById(claimStageLinkId);
        logger.debug("DocumentUtil::updateDocumentDetails responseData : {} --End",claimStageLink.get());
        return claimStageLink.orElse(null);
    }
}
