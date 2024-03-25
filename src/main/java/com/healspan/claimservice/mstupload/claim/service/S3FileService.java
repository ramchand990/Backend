package com.healspan.claimservice.mstupload.claim.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {
    ResponseEntity uploadFile(Long documentId,Long claimStageLinkId, MultipartFile file)throws Exception;

    public void deleteFile(String path);

    boolean exists(String path);
}
