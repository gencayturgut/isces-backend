package com.ISCES.service;

import com.ISCES.entities.*;
import com.ISCES.repository.*;
import com.ISCES.request.CandidacyRequest;
import com.ISCES.response.ApplyCandidacyResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyCandidacyService {
    private final FolderRepo folderRepo;

    private final StudentRepo studentRepo;

    private final CandidateRepo candidateRepo;

    private final AdminService adminService;

    private final ElectionRepo electionRepo;


    private final String uploadDir = "C:\\Users\\Ahmet\\Pictures\\Desktop\\CandidateDocuments";

    @Transactional
    public ApplyCandidacyResponse uploadFile(CandidacyRequest request) throws Exception {

        Student studentEntity = studentRepo.findByStudentNumber(request.getStudentNumber());
        if (studentEntity == null) {
            throw new Exception("Student not found!");
        }

        // Create new FolderEntity and save it to the database
        Folder uploadedFolder = new Folder();
        folderRepo.save(uploadedFolder);

        Student appliedStudent = studentEntity;

        List<File> uploadedFiles = new ArrayList<>();

        // Create a new directory for this student's files
        String studentDir = uploadDir + "\\" + appliedStudent.getStudentNumber();  // Or any other unique identifier for the student
        java.io.File directory = new java.io.File(studentDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        for (MultipartFile file : request.getFiles()) {
            try {
                // Save file to disk
                Path filePath = Paths.get(studentDir, file.getOriginalFilename());
                file.transferTo(filePath.toFile());

                // Create new FileEntity and associate it with the folder
                File uploadedFile = new File();
                uploadedFile.setFilePath(filePath.toString());
                uploadedFile.setFolder(uploadedFolder);
                uploadedFiles.add(uploadedFile);
            } catch (IOException e) {
                e.printStackTrace();
                ApplyCandidacyResponse response = ApplyCandidacyResponse.builder()
                        .isFolderSaved(false)
                        .isDepartmentChairApproved(false)
                        .build();
                return response;
            }
        }

        uploadedFolder.setFiles(uploadedFiles);
        uploadedFolder.setStudent(appliedStudent);
        folderRepo.save(uploadedFolder);

        ApplyCandidacyResponse response = ApplyCandidacyResponse.builder()
                .isFolderSaved(true)
                .isDepartmentChairApproved(false)
                .build();

        // Send the student's folder to department chair within the student information.
        //notifyDepartmentChair(appliedStudent, uploadedFolder, response);

        return response;

    }




}
