package com.ISCES.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.Delete;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Getter
@Setter
@Service
public class AWSService {

    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private StudentService studentService;

    @Value("${aws.bucketname}")
    private String bucketName;
    private String cubeName = "ye5acm9chphs/public/";



    public File convertMultiPartFileToFile(MultipartFile multipartFile){
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream outputStream = new FileOutputStream(file)){
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) { }
        return file;
    }

    public void uploadDocument(List<MultipartFile> multipartFiles, Long studentNumber) throws IOException {

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                File file = convertMultiPartFileToFile(multipartFile);
                String key = String.valueOf(cubeName + studentNumber + "/" + file.getName());
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
                amazonS3.putObject(putObjectRequest);
            } catch (AmazonServiceException e) {
            }
        }
    }



    //Election bittiğinde ya da cancel edildiğinde tüm dosyaları siliyor
    public void deleteFolders() {
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(cubeName);
        List<String> keys = new ArrayList<>();
        ObjectListing objects = amazonS3.listObjects(request);
        for (; ; ) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            if (summaries.size() < 1) {
                break;
            }
            summaries.forEach(s -> keys.add(s.getKey()));
            objects = amazonS3.listNextBatchOfObjects(objects);
        }
        for(String key: keys){
            amazonS3.deleteObject(bucketName, key);
        }

    }

    //bu sadece kontrol etmek amaçlı var gereksiz bir fonksiyon
    public ObjectListing checkList() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(cubeName);

        ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);
        return objectListing;
    }

    //Burası bitti her öğrencinin kendine ait tek bir dosyası olacağı için ve seçim bittikten ya da cancel
    //edildikten sonra dosyalar silineceği için database e ayrı bir path kaydetmeye gerek yok bence
    public List<String> listObjects(Long studentNumber){
        String key = String.valueOf(cubeName + studentNumber + "/");
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName).withPrefix(key);
        List<String> keys = new ArrayList<>();
        ObjectListing objects = amazonS3.listObjects(request);
        for (;;) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            if (summaries.size() < 1) {
                break;
            }
            summaries.forEach(s -> keys.add(s.getKey()));
            objects = amazonS3.listNextBatchOfObjects(objects);
        }

        return keys;
    }


    //Burası bitti değişmesi gereken bir şey yok
    public ResponseEntity<byte[]> downloadDocument(Long studentNumber) throws IOException {
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(zipOutputStream);

        for (String key : listObjects(studentNumber)) {
            try (S3Object responseInputStream = amazonS3.getObject(bucketName, key)) {
                S3Object objectInputStream = responseInputStream;
                byte[] fileBytes = objectInputStream.getObjectContent().readAllBytes();
                String[] temp = key.split("/");
                zip.putNextEntry(new ZipEntry(temp[temp.length-1]));
                zip.write(fileBytes);
                zip.closeEntry();
            }
        }

        zip.finish();
        zip.close();

        byte[] zipBytes = zipOutputStream.toByteArray();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"Candidate:"+ studentService.findByStudentNumber(studentNumber).getFirstName()+ ":documents.zip\"")
                .body(zipBytes);
    }

}