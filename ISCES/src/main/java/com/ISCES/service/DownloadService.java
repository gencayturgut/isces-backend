package com.ISCES.service;

import java.io.*;
import java.util.List;
import java.util.zip.*;

import com.ISCES.entities.Folder;
import com.ISCES.entities.Student;
import com.ISCES.repository.FolderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {
    @Autowired
    private FolderRepo folderRepo;

    public ResponseEntity<InputStreamResource> downloadStudentFolders(Long studentNumber) throws IOException {
        // Find the student's folders
        List<com.ISCES.entities.Folder> folders = folderRepo.findByStudent_StudentNumber(studentNumber);
        Student student = folders.get(0).getStudent();
        // Temporary file to hold the zip
        File zipFile = File.createTempFile(student.getFirstName()+"_"+student.getLastName()+"_"+student.getStudentNumber().toString(), ".zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            // Loop over each folder
            for (Folder folder : folders) {
                // Loop over each file in the folder
                for (com.ISCES.entities.File fileEntity : folder.getFiles()) {
                    File file = new File(fileEntity.getFilePath());
                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                    fis.close();
                }
            }
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=StudentFolders.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
