package com.healspan.claimservice.mstupload.claim.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.healspan.claimservice.mstupload.claim.repo.DocumentRepo;
import com.healspan.claimservice.mstupload.claim.util.DocumentUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Service
public class S3FileServiceImpl implements S3FileService {

   private Logger logger = LoggerFactory.getLogger(S3FileServiceImpl.class);
    @Value("${AWSAccessKeyId}")
    private String accessKeyId;
    @Value("${AWSSecretKey}")
    private String secretKey;
    @Value("${s3bucketname}")
    private String bucketname;

    @Autowired
    private DocumentUtil documentUtil;
    @Autowired
    private DocumentRepo documentRepo;

    public ResponseEntity uploadFile(Long documentId, Long claimInfoId, MultipartFile multipartFile){
        logger.debug("S3FileServiceImpl::uploadFile requested documentId: {},claimInfoId: {} and multipartFile: {} --Start",documentId,claimInfoId,multipartFile);
        File file = null;
        try {
            //get document details

                String currentPath = new File(".").getCanonicalPath();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                String currentDate = formatter.format(new Date());

                logger.debug("Convert the Multipart file received in request to regular file.");
                file = convertMultipartFile(multipartFile, Paths.get(currentPath));
                long timestamp = System.currentTimeMillis();
                logger.debug("Constructed file path for claimInfoId:{}, and documentName: {}", claimInfoId, timestamp + "_" + file.getName());
                String path = currentDate + "/" + claimInfoId;

                logger.debug("Access AWS S3 service and upload the document to S3 on path: {}", path);
                AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);
                AmazonS3 s3client = AmazonS3ClientBuilder
                        .standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion(Regions.AP_SOUTH_1)
                        .build();

                PutObjectResult obj = s3client.putObject(bucketname, path + "/" + timestamp + "_" + file.getName(), file);
                logger.debug("Document uploaded to S3.");

                // update the document table with path and filename
                documentUtil.updateDocumentDetails(documentId, timestamp + "_" + file.getName(), path);
            logger.debug("S3FileServiceImpl::uploadFile response Status: {} --End",ResponseEntity.ok().build());
                return ResponseEntity.ok().build();

        }catch(Exception e){
            logger.info("Exception occurred:\n ", ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }finally {
            // Delete locally created file
            if (file.exists())
                file.delete();
        }
    }

    private File convertMultipartFile(MultipartFile multipart, Path dir) throws IOException {
        logger.debug("S3FileServiceImpl::convertMultipartFile requested multipart:{} and dir:{} --Start",multipart,dir);
        Path filepath = Paths.get(dir.toString(), multipart.getOriginalFilename());
        multipart.transferTo(filepath);
        logger.debug("S3FileServiceImpl::convertMultipartFile response file:{} --End",filepath.toFile());
        return filepath.toFile();
    }

    @Override
    public void deleteFile(String path){
        logger.debug("S3FileServiceImpl::deleteFile requestedPath: {} --Start",path);
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
        logger.info("Delete file from aws s3 for filePath:{}",path);
        s3client.deleteObject(bucketname,path);
        logger.debug("S3FileServiceImpl::deleteFile --End");
    }

    @Override
    public boolean exists(String path) {
        logger.debug("S3FileServiceImpl::exists requestedPath:{} --Start",path);
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
        logger.debug("S3FileServiceImpl::exists responseData:{} --End",bucketname+"and"+path);
        return s3client.doesObjectExist(bucketname,path);

    }
}