package com.ly.add.sponge.tcel.utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UploadUtil {
    // 线下 accessKey, secretKey
    private static final String ACCESS_KEY = "9GVPRLZJ9M586U5IDW8Y";
    private static final String ACCESS_SECRET_KEY = "HLVW7X6X6XVx2mMOXxOg60601ZDYAvjebj4IWI96";
    // 线下END_POINT为 http://10.100.156.232:7480;
    // 线上END_POINT为 http://tcstore1.17usoft.com, 无需配置，只有内网才能访问此域名。
    private static final String END_POINT = "tcstore1.17usoft.com";
    private static AmazonS3Client s3client = null;
    
    private static UploadUtil instance = new UploadUtil();
    private UploadUtil() {
        if(s3client == null){
            AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, ACCESS_SECRET_KEY);
            ClientConfiguration clientCfg = new ClientConfiguration();
            clientCfg.setProtocol(Protocol.HTTP);

            s3client = new AmazonS3Client(credentials, clientCfg);
            s3client.setEndpoint(END_POINT);
            s3client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
        }
    }

    public static UploadUtil getInstance(){
        return instance;
    }

    public void listObj(String bucket) {
        ObjectListing objects = s3client.listObjects(bucket);
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                System.out.println(objectSummary.getKey() + "\t" + objectSummary.getSize() + "\t"
                        + StringUtils.fromDate(objectSummary.getLastModified()));
            }
            objects = s3client.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());
    }

    public boolean doesObjectExist(String bucket, String key){
        ObjectListing objects = s3client.listObjects(bucket);
        do {
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                if(objectSummary.getKey().equals(key))
                    return true;
            }
            objects = s3client.listNextBatchOfObjects(objects);
        } while (objects.isTruncated());

        return false;
    }

    public String getURL(String bucket, String obj) {
        URL url = s3client.getUrl(bucket, obj);
        return url == null ? "Null URL" : url.toString();
    }

    public boolean putObjByFile(String bucket, String key, String localFile){
        try {
            PutObjectRequest request = new PutObjectRequest(bucket, key, new File(localFile));
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(new File(localFile).length());
            request.setMetadata(meta);
            s3client.putObject(request);
        } catch (AmazonClientException clientException) {
            return false;
        }
        return true;
    }

    public boolean putObjByStream(String bucket,  String key, byte[] array) throws IOException{
        InputStream input = new ByteArrayInputStream(array);
        try{
            putObjByStream(bucket, key, input);
        }catch(AmazonClientException clientException){
            return false;
        }
        return true;
    }

    public boolean putObjByStream(String bucket, String key, InputStream input) throws IOException{
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(input.available());
        PutObjectRequest request = new PutObjectRequest(bucket, key, input, meta);
        request.setCannedAcl(CannedAccessControlList.PublicRead);
        try{
            s3client.putObject(request);
        }catch(AmazonClientException clientException){
            return false;
        }
        return true;
    }

    public String doUpload(InputStream input, String fileName) throws IOException {
        String key = "file/" + fileName;
        String bucket = "bms";
        putObjByStream(bucket, key, input);
        return getURL(bucket, key);
    }

    public String doUploadImg(InputStream input, String fileName) throws IOException {
        String key = "file/" + fileName;
        String bucket = "bms";
        putImgByStream(bucket, key, input);
        return getURL(bucket, key);
    }

    public boolean putImgByStream(String bucket, String key, InputStream input) throws IOException{
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(input.available());
        meta.setContentType("image/png");
        PutObjectRequest request = new PutObjectRequest(bucket, key, input, meta);
        request.setCannedAcl(CannedAccessControlList.PublicRead);
        try{
            s3client.putObject(request);
        }catch(AmazonClientException clientException){
            return false;
        }
        return true;
    }

}
