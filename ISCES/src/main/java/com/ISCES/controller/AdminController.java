package com.ISCES.controller;

import com.ISCES.entities.*;
import com.ISCES.request.ElectionRequest;
import com.ISCES.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@RestController
@CrossOrigin("https://iztechelectionfrontend.herokuapp.com")
// Mail atma kısımları burda deploy etmeden önce bağlamalıyız!!!!!!!!
public class AdminController {// Bütün return typeler değişebilir . Response ve Request packageına yeni classlar eklenmeli frontendden hangi bilgi istendiğine göre


    private CandidateService candidateService;
    private UserService userService;
    private AdminService adminService;
    private StudentService studentService;
    private ElectionService electionService;
    private DelegateService delegateService;
    private EmailService emailService;
    private Email2Service email2Service;
    private FolderService folderService;
    private DownloadService downloadService;


    public AdminController(DownloadService downloadService,FolderService folderService,Email2Service email2Service, EmailService emailService, DelegateService delegateService, CandidateService candidateService, UserService userService, StudentService studentService, AdminService adminService,ElectionService electionService) {
        this.emailService = emailService;
        this.candidateService = candidateService;
        this.userService = userService;
        this.studentService = studentService;
        this.adminService = adminService;
        this.electionService = electionService;
        this.delegateService = delegateService;
        this.email2Service = email2Service;
        this.folderService = folderService;
        this.downloadService = downloadService;
    }




    @GetMapping("/unevaluatedStudents/{departmentId}") // departmentId is departmentİd of officer and unevaluatedStudents
    public List<Student> unevaluatedStudents(@PathVariable  Long departmentId){
        return studentService.findByDepartmentIdAndIsAppliedForCandidacyAndUser_Role(departmentId,true,"student"); //  it returns the students who is not approved and not disapproved yet.
    }                                                                                                         //  true means this student is applied for candidacy.


                                                         // departmentId is departmentİd of officer and unevaluatedStudents
    @GetMapping("/showConfirmedStudents/{departmentId}") // confirmedStudents means candidates
    public List<Candidate> getConfirmedStudents(@PathVariable  Long departmentId){
        return candidateService.findCandidateByDepartmentId(departmentId,false); //  it returns the candidate  list grouped by officer who are approved.
    }



                                                        // departmentId is departmentİd of officer and unevaluatedStudents
    @GetMapping("/showRejectedStudents/{departmentId}") // if we need this for officer, we should this implement again...!!!
    public List<Student> getRejectedStudents(@PathVariable  Long departmentId){
        return studentService.findByDepartmentIdAndIsAppliedForCandidacyAndUser_Role(departmentId,null,"student"); //  it returns the students who are rejected and sets isApplied null
                                                                                                              //   because isApplied of rejected student  is null.
    }





    @GetMapping("/confirmStudent/{studentNumber}") // it returns candidate because application is confirmed. Student is candidate right now.
    public Candidate confirmStudent(@PathVariable Long studentNumber){
        Student tempStudent = studentService.findByStudentNumber(studentNumber);
        Candidate tempCandidate = new Candidate();
        if(tempStudent.getIsAppliedForCandidacy() && !tempStudent.getUser().getRole().equals("candidate")){
            Election currentElection = electionService.findByIsFinished(false); // current election
            Long tempCandidateId = Long.valueOf(candidateService.getAllCandidates().size()) + 1; // it's to assign an id to the candidate.
            tempCandidate.setCandidateId(tempCandidateId);
            tempCandidate.setStudent(tempStudent);
            tempCandidate.setVotes(Long.valueOf(0));
            tempCandidate.setElection(currentElection);
            tempCandidate.getStudent().getUser().setRole("candidate"); // user role is changed as candidate
            Candidate savedCandidate = candidateService.save(tempCandidate);
            emailService.sendEmail(studentService.findByStudentNumber(studentNumber).getUser().getEmail(),true); //  sends email for confirmed students.
            return candidateService.save(tempCandidate); // it returns the candidate who is approved by officer.
        }
        return tempCandidate;
    }


    @GetMapping("/rejectStudent/{studentNumber}") //  it returns student . If applications is not approved by officer , student is still student not candidate!!
    public Student rejectStudent(@PathVariable Long studentNumber){// candidacy of candidate is disapproved.
        if(studentService.findByStudentNumber(studentNumber).getIsAppliedForCandidacy() &&
            !studentService.findByStudentNumber(studentNumber).getUser().getRole().equals("candidate")){
            studentService.findByStudentNumber(studentNumber).setIsAppliedForCandidacy(null); // isAppliedCandidacy of student is changed to null
            emailService.sendEmail(studentService.findByStudentNumber(studentNumber).getUser().getEmail(),false);
            return studentService.save(studentService.findByStudentNumber(studentNumber));// It returns and saves the student who is rejected by officer.
        }
       //  sends email for rejected students
        return null;
    }




    @GetMapping("/enterElectionDate/{startDate}/{endDate}") //  rector enters election date.
    public ResponseEntity<ElectionRequest> enterElectionDate(@PathVariable String startDate,@PathVariable String endDate){
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end= LocalDateTime.parse(endDate);
        ElectionRequest electionRequest=new ElectionRequest(start,end);
        LocalDateTime now = LocalDateTime.now();// current date
        Long electionId;
        Election tempElection = new Election();
        if(!electionService.isEnteredElectionDateByRector()) { // if rector didn't set an election.
            if (electionRequest.getStartDate().isAfter(now) && electionRequest.getEndDate().isAfter(now) && electionRequest.getStartDate().isBefore(electionRequest.getEndDate())) { // if rector enters notvalid date for now.
                electionId = Long.valueOf(electionService.getAllElections().size() + 1);
                tempElection.setElectionId(electionId);
                tempElection.setFinished(false);
                tempElection.setStartDate(electionRequest.getStartDate());
                tempElection.setEndDate(electionRequest.getEndDate());
                for (Student student : studentService.getAllStudents()) {
                    if (student.isVoted()) { //  isVoted of voters are changed to false  for next year election
                        student.setVoted(false);
                        studentService.save(student);
                    }
                    if(student.getIsAppliedForCandidacy() == null){
                        student.setIsAppliedForCandidacy(false);
                        studentService.save(student);
                    }
                    if(student.getIsAppliedForCandidacy()) { // changed to false for all students
                        student.setIsAppliedForCandidacy(false);
                        studentService.save(student);
                    }
                    if (student.getUser().getRole().equals("candidate") || student.getUser().getRole().equals("representative")) { //  changed false for  next year election
                        student.getUser().setRole("student");
                        studentService.save(student);
                    }

                }
                try {
                    electionService.save(tempElection);
                    for(Student student: studentService.getAllStudents()){ //  sends all students election start date and end date.
                        email2Service.sendEmail(student.getUser().getEmail(),electionRequest.getStartDate(),electionRequest.getEndDate());
                    }
                    return new ResponseEntity<>(new ElectionRequest(electionRequest.getStartDate(), electionRequest.getEndDate()), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(new ElectionRequest("Election couldn't be setted."), HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(new ElectionRequest("Entered date is not valid. Try to enter different end date or start date"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ElectionRequest("Rector has already setted an election"), HttpStatus.BAD_REQUEST);
    }



    @GetMapping("/electionDate")
    public Election getElectionDate(){return electionService.findByIsFinished(false);
    }



    @GetMapping("/concludeTie/{delegateId}") // select a candidate to make him confirmed delegate by delegate id.
    public void concludeTie(@PathVariable Long delegateId){
        Delegate delegate = delegateService.findByDelegateId(delegateId);
        Long departmentIdOfDelegate = delegate.getCandidate().getStudent().getDepartment().getDepartmentId();
        delegate.setIsConfirmed(true);
        delegateService.save(delegate);
        List<Delegate> delegateList = delegateService.findUnconfirmedCandidatesByDepartmentIdAndIsConfirmed(departmentIdOfDelegate);
        for(Delegate tempDelegate : delegateList){
            tempDelegate.setIsConfirmed(false);
            delegateService.save(tempDelegate);
        }
    }


    @GetMapping("/finishElection")
    public Election finishElection(){ //  cancels election
        Election election = electionService.getAllElections().get(electionService.getAllElections().size() - 1);
        List<Candidate> candidateList = candidateService.findByElectionId(election.getElectionId());
        for(Candidate candidate: candidateList){
            candidate.getStudent().getUser().setRole("student");
            candidateService.deleteCandidate(candidate);
        }
        // buraya de electionın bittiğinde userlara sonuçlara bakabileceğini söyleyen bir mail yollamamız lazım !!!!!!!!!!!!
        if(!election.isFinished()){ // if isFinished of last election is false  -> if election hasn't ended yet.
            electionService.delete(election); // save election to database
        }
        return election; // returns finished election.
    }



    @GetMapping("/downloadStudentFiles/{studentNumber}")
    public ResponseEntity<InputStreamResource> downloadStudentFolders(@PathVariable Long studentNumber) throws IOException, IOException {
        return downloadService.downloadStudentFolders(studentNumber);
    }


}
