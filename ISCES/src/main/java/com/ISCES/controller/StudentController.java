package com.ISCES.controller;


import com.ISCES.entities.Candidate;
import com.ISCES.entities.Student;
import com.ISCES.repository.FileRepo;
import com.ISCES.repository.FolderRepo;
import com.ISCES.request.CandidacyRequest;
import com.ISCES.response.ApplyCandidacyResponse;
import com.ISCES.response.VoteResponse;
import com.ISCES.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RestController
@CrossOrigin("https://iztechelectionfrontend.herokuapp.com")
public class StudentController { // Bütün return typeler değişebilir . Response ve Request packageına yeni classlar eklenmeli frontendden hangi bilgi istendiğine göre

    private StudentService studentService;
    private CandidateService candidateService;
    private ElectionService electionService;
    private ApplyCandidacyService applyCandidacyService;
    private FolderRepo folderRepo;
    private FileRepo fileRepo;
    private AWSService awsService;

    public StudentController(StudentService studentService, CandidateService candidateService,ElectionService electionService,AWSService awsService, FolderRepo folderRepo, FileRepo fileRepo, ApplyCandidacyService applyCandidacyService) {
        this.studentService = studentService;
        this.candidateService = candidateService;
        this.electionService = electionService;
        this.fileRepo = fileRepo;
        this.folderRepo = folderRepo;
        this.applyCandidacyService = applyCandidacyService;
        this.awsService =  awsService;
    }


    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/getStudent/{studentNumber}")
    public Student findStudentById(@PathVariable Long studentNumber) {
        return studentService.findByStudentNumber(studentNumber);
    }

    // GETMAPPING DEĞİŞECEK
    @GetMapping("/vote/{studentNumber}/{candidateId}")
    // studentNumber is voter's number, departmentıd "is candidate's id.
    public ResponseEntity<VoteResponse> vote(@PathVariable Long studentNumber, @PathVariable Long candidateId) {
        String message = "Couldn't vote";
        List<Candidate> candidateList = candidateService.findCandidateByDepartmentId(studentService.findByStudentNumber(studentNumber).getDepartment().getDepartmentId(),false);
        if (!studentService.findByStudentNumber(studentNumber).isVoted() &&
                studentService.findByStudentNumber(studentNumber).getDepartment().equals(candidateService.findById(candidateId).getStudent().getDepartment())){
            // if student didn't vote   and departmentId of student and departmentId of candidate is equal.
            studentService.findByStudentNumber(studentNumber).setVoted(true); //  the isVoted of voter is changed.
            candidateService.getVote(candidateService.findById(candidateId)); // candidate's vote += 1
            message = "Student which has id: " + studentNumber + " voted.";

            return new ResponseEntity<>(new VoteResponse(message, studentNumber), HttpStatus.OK);
        }
        return new ResponseEntity<>(new VoteResponse(message), HttpStatus.BAD_REQUEST);
    }
        // GETMAPPING DEĞİŞECEK


    @GetMapping("/applyToBeCandidate/{studentNumber}")// it's for students to apply to be a candidate         !!!!!!!!! BELGE EKLEME YAPARKEN BU KISIMDA DEĞİŞİKLİK YAPILACAK !!!!!
    public ResponseEntity<CandidacyRequest> applyToBeCandidate(@PathVariable Long studentNumber, @RequestParam("transcript") MultipartFile transcript, @RequestParam("criminal") MultipartFile criminal) {
        LocalDateTime now = LocalDateTime.now();
        if(electionService.isEnteredElectionDateByRector()){
            if ((studentService.findByStudentNumber(studentNumber).getIsAppliedForCandidacy() != null) &&
                    (!studentService.findByStudentNumber(studentNumber).getIsAppliedForCandidacy())) { // if student didn't apply for candidacy or didn't rejected by officer (isApplied != false//                                                                      isApplied != null)
                if (!electionService.isThereStartedElection(now)) {
                    Student tempStudent = studentService.findByStudentNumber(studentNumber);
                    studentService.findByStudentNumber(studentNumber).setIsAppliedForCandidacy(true);// The isAppliedForCandidacy of the student applying for candidacy has been changed.
                    studentService.save(studentService.findByStudentNumber(studentNumber)); // changes are saved for this student.
                    if (studentService.findByStudentNumber(studentNumber).getGrade() > 2.50){

                        CandidacyRequest candidacyRequest = new CandidacyRequest(studentNumber, "Your application is succesful!"); // it's for student who is not applied for candidacy before for this election.
                        try {
                            List<MultipartFile> multipartFiles = new ArrayList<>();
                            multipartFiles.add(transcript);
                            multipartFiles.add(criminal);
                            awsService.uploadDocument(multipartFiles, studentNumber);
                        } catch (IOException e) {
                        }
                        return ResponseEntity.ok(candidacyRequest);
                    }
                    CandidacyRequest gradeRequest = new CandidacyRequest("Your gpa is not enough to apply to be a candidate "); // it's for student who is not applied for candidacy before for this election.
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gradeRequest);
                } else {
                    CandidacyRequest cantApplyRequest = new CandidacyRequest("Election is already started. You can not apply to be candidate");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cantApplyRequest);
                }
            }
            CandidacyRequest notAppliedRequest = new CandidacyRequest("This student has already applied for candidacy!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notAppliedRequest); //  message for student who is applied for candidacy before for this election
        }
        CandidacyRequest notAppliedRequest = new CandidacyRequest("Rector has not setted an election yet");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(notAppliedRequest);
    }







    @PostMapping("/uploadFolder")
    public ResponseEntity<ApplyCandidacyResponse> uploadFolder(@RequestParam("studentNumber") Long studentNumber,
                                                                  @RequestParam("files") MultipartFile[] files) throws Exception {
        CandidacyRequest request = new CandidacyRequest(studentNumber,files);
        ApplyCandidacyResponse response = applyCandidacyService.uploadFile(request);
        try {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}