package com.healspan.claimservice.mstupload.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.healspan.claimservice.mstupload.claim.dao.business.Document;
import com.healspan.claimservice.mstupload.claim.dto.business.DocumentDownloadDto;
import com.healspan.claimservice.mstupload.claim.dto.report.ReportResponse;
import com.healspan.claimservice.mstupload.claim.repo.ClaimStageLinkRepo;
import com.healspan.claimservice.mstupload.claim.repo.DocumentRepo;
import com.healspan.claimservice.mstupload.claim.service.ReportService;
import com.healspan.claimservice.mstupload.claim.service.S3FileService;
import com.healspan.claimservice.mstupload.claim.util.DocumentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class S3BucketDownloadServiceImpl implements S3BucketDownloadService {

    private final Logger logger = LoggerFactory.getLogger(S3BucketDownloadServiceImpl.class);
    @Autowired
    DocumentUtil documentUtil;
    @Autowired
    private S3FileService s3FileService;
    @Value("${AWSAccessKeyId}")
    private String accessKeyId;
    @Value("${AWSSecretKey}")
    private String secretKey;
    @Value("${s3bucketname}")
    private String bucketName;
    @Autowired
    private DocumentRepo documentRepo;
    @Autowired
    private ClaimStageLinkRepo claimStageLinkRepo;

    @Autowired
    private ReportService reportService;

    public DocumentDownloadDto downloadFile(Long documentId) {
        logger.debug("S3BucketDownloadServiceImpl::downloadFile requested documentId: {} --Start", documentId);
        DocumentDownloadDto dto = null;
        try {
            //get document
            logger.info("Get document details for documentId:{}", documentId);
            Document document = documentUtil.getDocumentDetails(documentId);
            logger.info("DocumentUtil : getDocumentDetails --End");
            if (document != null) {
                logger.info("Document found in db for DocumentId: {} ", document.getId());
                dto = new DocumentDownloadDto();
                String path = document.getDocumentPath();
                String fileName = document.getDocumentName();
                dto.setFileName(fileName);
                System.out.println("PATH:" + path + fileName);
                logger.info("Access AWS S3 service and fetch the document from S3.");
                AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);
                AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
                S3Object documentObject = s3client.getObject(new GetObjectRequest(bucketName, path + "/" + fileName));

                if (documentObject != null) {
                    logger.info("Document object retrieved from S3.");

                    InputStream is = documentObject.getObjectContent();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int len;
                    byte[] buffer = new byte[4096];

                    logger.info("Read the file data using S3 object and return the OutputStream.");
                    while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    dto.setByteArrayOutputStream(outputStream);
                    return dto;
                } else
                    logger.info("Document object retrieval from S3 failed.");
            } else
                logger.info("Document NOT FOUND in db for DocumentId: {} ", document.getId());

        } catch (IOException ioException) {
            logger.error("IOException: " + ioException.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException Message:    " + serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
        logger.debug("S3BucketDownloadServiceImpl::downloadFile responseData: {} --End", dto);
        return dto;
    }

    @Override
    public byte[] downloadZipFile(long claimId) throws Exception {
        final String reportName = "healspan.jrxml";
        logger.info("S3BucketDownloadServiceImpl::downloadZipFile requested claimId: {} --Start", claimId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipFiles = new ZipOutputStream(byteArrayOutputStream);
        Map<String, Document> docList = new HashMap<>();
        Map<String, String> orgFileList = new HashMap<>();
        logger.info("Find all claimStageLinkId records in db for claimId:{}", claimId);
        List<Long> claimStageLinks = claimStageLinkRepo.findAllByOpenQueryTransaction(claimId);
        List<Document> documents = documentRepo.findAllByClaimStageLinkIdIn(claimStageLinks);

        if (!documents.isEmpty()) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);
            AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
            for (Document document : documents) {
                logger.info("trying to read document:{}", document);
                String path = document.getDocumentPath() + "/" + document.getDocumentName();
                if (document.getDocumentName() != null && document.getDocumentPath() != null && s3FileService.exists(path)) {
                    logger.info("check whether file is present in bucket or not: {}", s3FileService.exists(path));
                    if (!docList.containsKey(document.getDocumentName())) {
                        orgFileList.put(document.getDocumentName(), document.getDocumentName());
                        docList.put(document.getDocumentName(), document);
                    } else {
                        long milliTime = System.currentTimeMillis();
                        orgFileList.put(milliTime + "_" + document.getDocumentName(), document.getDocumentName());
                        document.setDocumentName(milliTime + "_" + document.getDocumentName());
                        docList.put(document.getDocumentName(), document);
                    }
                }
            }
            for (Map.Entry obj : docList.entrySet()) {
                Document document = (Document) obj.getValue();

                String path = document.getDocumentPath();
                String fileName = orgFileList.get(obj.getKey());
                S3Object documentObject = s3client.getObject(new GetObjectRequest(bucketName, path + "/" + fileName));
                InputStream is = documentObject.getObjectContent();
                zipFiles.putNextEntry(new ZipEntry((String) obj.getKey()));
                int len;
                byte[] buffer = new byte[4096];
                logger.info("Read the file data using S3 object and return the OutputStream.");
                while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                    zipFiles.write(buffer, 0, len);
                }

            }
        }
        ReportResponse responseData = reportService.patientReport(reportName, claimId);
        if (responseData != null) {
            zipFiles.putNextEntry(new ZipEntry(responseData.getFileName()));
            zipFiles.write(responseData.getReportData());
        }
        zipFiles.finish();
        zipFiles.close();
        logger.debug("S3BucketDownloadServiceImpl::downloadZipFile responseData: {} --End", claimId);
        return byteArrayOutputStream.toByteArray();
    }
}