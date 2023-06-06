package com.ISCES.controller;

import com.ISCES.entities.*;
import com.ISCES.request.ElectionRequest;
import com.ISCES.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@RestController
public class AdminController {// Bütün return typeler değişebilir . Response ve Request packageına yeni classlar eklenmeli frontendden hangi bilgi istendiğine göre


    private CandidateService candidateService;
    private UserService userService;
    private AdminService adminService;
    private StudentService studentService;
    private ElectionService electionService;
    private DelegateService delegateService;
    private EmailService emailService;
    private Email2Service email2Service;


    public AdminController(Email2Service email2Service, EmailService emailService, DelegateService delegateService, CandidateService candidateService, UserService userService, StudentService studentService, AdminService adminService,ElectionService electionService) {
        this.emailService = emailService;
        this.candidateService = candidateService;
        this.userService = userService;
        this.studentService = studentService;
        this.adminService = adminService;
        this.electionService = electionService;
        this.delegateService = delegateService;
        this.email2Service = email2Service;
    }



    @GetMapping("/unevaluatedStudents/{departmentId}") // departmentId is departmentİd of officer and unevaluatedStudents
    public List<Student> unevaluatedStudents(@PathVariable  Long departmentId){
        return studentService.findByDepartmentIdAndIsAppliedForCandidacyAndUser_Role(departmentId,true,"student"); //  it returns the students who is not approved and not disapproved yet.
    }                                                                                                         //  true means this student is applied for candidacy.


                                                         // departmentId is departmentİd of officer and unevaluatedStudents
    @GetMapping("/showConfirmedStudents/{departmentId}") // confirmedStudents means candidates
    public List<Candidate> getConfirmedStudents(@PathVariable  Long departmentId){
        return candidateService.findCandidateByDepartmentId(departmentId); //  it returns the candidate  list grouped by officer who are approved.
    }


                                                        // departmentId is departmentİd of officer and unevaluatedStudents
    @GetMapping("/showRejectedStudents/{departmentId}") // if we need this for officer, we should this implement again...!!!
    public List<Student> getRejectedStudents(@PathVariable  Long departmentId){
        return studentService.findByDepartmentIdAndIsAppliedForCandidacyAndUser_Role(departmentId,null,"student"); //  it returns the students who are rejected and sets isApplied null
                                                                                                              //   because isApplied of rejected student  is null.
    }



    // GET MAPPING DEĞİŞECEK -> PostMapping
    @GetMapping("/confirmStudent/{studentNumber}") // it returns candidate because application is confirmed. Student is candidate right now.
    public Candidate confirmStudent(@PathVariable Long studentNumber){
        Student tempStudent = studentService.findByStudentNumber(studentNumber);
        Candidate tempCandidate = new Candidate();
        if(tempStudent.getIsAppliedForCandidacy() && !tempStudent.getUser().getRole().equals("candidate")){
            Long tempCandidateId = Long.valueOf(candidateService.getAllCandidates().size()) + 1; // it's to assign an id to the candidate.
            tempCandidate.setCandidateId(tempCandidateId);
            tempCandidate.setStudent(tempStudent);
            tempCandidate.setVotes(Long.valueOf(0));
            tempCandidate.getStudent().getUser().setRole("candidate"); // user role is changed as candidate
            Candidate savedCandidate = candidateService.save(tempCandidate);
            return candidateService.save(tempCandidate); // it returns the candidate who is approved by officer.
        }
        emailService.sendEmail(studentService.findByStudentNumber(studentNumber).getUser().getEmail(),true); //  sends email for confirmed students.
        return tempCandidate;
    }

    // GET MAPPING DEĞİŞECEK -> PutMapping
    @GetMapping("/rejectStudent/{studentNumber}") //  it returns student . If applications is not approved by officer , student is still student not candidate!!
    public Student rejectStudent(@PathVariable Long studentNumber){// candidacy of candidate is disapproved.
        if(studentService.findByStudentNumber(studentNumber).getIsAppliedForCandidacy() &&
            !studentService.findByStudentNumber(studentNumber).getUser().getRole().equals("candidate")){
            studentService.findByStudentNumber(studentNumber).setIsAppliedForCandidacy(null); // isAppliedCandidacy of student is changed to null
            return studentService.save(studentService.findByStudentNumber(studentNumber));// It returns and saves the student who is rejected by officer.
        }
        emailService.sendEmail(studentService.findByStudentNumber(studentNumber).getUser().getEmail(),false); //  sends email for rejected students
        return null;
    }


  @PostMapping("/enterElectionDate") //  rector enters election date.
   public ResponseEntity<ElectionRequest> enterElectionDate(@RequestBody ElectionRequest electionRequest){
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
                    } else if (student.getIsAppliedForCandidacy()) { // changed to false for all students
                        student.setIsAppliedForCandidacy(false);
                    } else if (student.getUser().getRole().equals("candidate")) { //  changed false for  next year election
                        student.getUser().setRole("student");
                    }
                }
                candidateService.deleteAll(); // candidates are deleted.
                delegateService.deleteAll(); // delegates are deleted.
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



}
