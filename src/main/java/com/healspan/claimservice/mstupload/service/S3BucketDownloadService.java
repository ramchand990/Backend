package com.healspan.claimservice.mstupload.service;

import com.healspan.claimservice.mstupload.claim.dto.business.DocumentDownloadDto;

import java.util.zip.ZipOutputStream;

public interface S3BucketDownloadService {

    public DocumentDownloadDto downloadFile(Long documentId) ;

    public byte[] downloadZipFile(long claimId) throws Exception;
}