package com.healspan.claimservice.mstupload.claim.controller;


import com.healspan.claimservice.mstupload.claim.dto.business.DocumentDownloadDto;
import com.healspan.claimservice.mstupload.service.S3BucketDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@CrossOrigin
@RequestMapping("/healspan/claim")
public class S3BucketDownloadController {
    @Autowired
    private S3BucketDownloadService service;

    private Logger logger = LoggerFactory.getLogger(S3BucketDownloadController.class);

    @GetMapping(value = "/download/{documentId}")
    public ResponseEntity downloadFile(@PathVariable Long documentId) {
        logger.info("Download file request received for DocumentId:{}",documentId);
        DocumentDownloadDto downloadFile = service.downloadFile(documentId);

        if (downloadFile == null){
            logger.info("Request processed but file not downloaded. ");
            return ResponseEntity.notFound().build();
        }
        logger.info("Request to download document processed successfully.");
        return ResponseEntity.ok()
                .contentType(contentType(downloadFile.getFileName()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFile.getFileName() + "\"")
                .body(downloadFile.getByteArrayOutputStream().toByteArray());
    }


    private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @GetMapping(value = "/downloadZip/{claimId}" )
    public ResponseEntity downloadFileZip(@PathVariable Long claimId)
    {
        logger.info("Request received to download zip file for claimId:{}",claimId);
        try {
           byte [] data = service.downloadZipFile(claimId);
           if(data != null && data.length >0) {
               return ResponseEntity.ok()
                       .contentType(MediaType.parseMediaType("application/zip"))
                       .header("Content-Disposition", "attachment; filename=\"" + System.currentTimeMillis() + ".zip" + "\"")
                       .body(data);
           }else {
               return new ResponseEntity("File Not Present ",HttpStatus.OK);
           }
        }catch (Exception ex)
        {
            logger.debug("Exception Details :{}",Arrays.asList(ex.getStackTrace()));
            ex.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
}