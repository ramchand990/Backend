package com.healspan.claimservice.mstupload.claim.controller;

import com.healspan.claimservice.mstupload.claim.service.ClaimOperationService;
import com.healspan.claimservice.mstupload.claim.service.S3FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/healspan/claim")
public class S3BucketUploadController {
    @Autowired
    private S3FileService s3FileService;

    @Autowired
    private ClaimOperationService claimOperationService;
    private Logger logger = LoggerFactory.getLogger(S3BucketUploadController.class);

    @GetMapping("/test")
    public String test() {
        return "hello world";
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam String inputDocId, @RequestParam String claimInfoId, @RequestParam("file") MultipartFile file) throws Exception {
        ResponseEntity responseEntity = null;
        Long documentId = null;

        try {
            logger.info("Upload document request received for DocumentId:{},claimInfoId:{} and file:{}", inputDocId,claimInfoId,file);
            if(inputDocId != null && !inputDocId.isEmpty()) {
                 documentId = Long.parseLong(inputDocId);
            }
            Long claimId = Long.parseLong(claimInfoId);

            responseEntity = s3FileService.uploadFile(documentId,claimId,file);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("Request to upload document processed successfully.");
                return new ResponseEntity<>(responseEntity, HttpStatus.OK);
            }
            logger.info("Request processed but file not downloaded. ");
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(responseEntity, HttpStatus.MOVED_PERMANENTLY);
    }
}
