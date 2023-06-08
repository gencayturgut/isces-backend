package com.ISCES.controller;


import com.ISCES.entities.*;
import com.ISCES.repository.DelegateRepo;
import com.ISCES.repository.DepartmentRepo;
import com.ISCES.response.LoginResponse;
import com.ISCES.response.isInEletionProcessResponse;
import com.ISCES.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RestController
@CrossOrigin("https://iztechelectionfrontend.herokuapp.com/")
public class UserController { // Bütün return typeler değişebilir . Response ve Request packageına yeni classlar eklenmeli frontendden hangi bilgi istendiğine göre


    private UserService userService;
    private CandidateService candidateService;

    private StudentService studentService;
    private AdminService adminService;
    private ElectionService electionService;

    private DepartmentRepo departmentRepo;

    private DelegateService delegateService;



    public UserController(DelegateService delegateService, DepartmentRepo  departmentRepo, UserService userService, CandidateService candidateService, StudentService studentService, AdminService adminService, ElectionService electionService) {
        this.userService = userService;
        this.candidateService = candidateService;
        this.studentService = studentService;
        this.adminService = adminService;
        this.electionService = electionService;
        this.departmentRepo = departmentRepo;
        this.delegateService = delegateService;}



    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }



    @GetMapping("/login/{email}/{password}")// user logins with email and password
    public ResponseEntity<LoginResponse> login(@PathVariable String email, @PathVariable String password) {
        LocalDateTime now = LocalDateTime.now();
        String controller = "";// message for frontend  (Logged-in )
        User user = userService.findByEmail(email);
        boolean isElectionStarted = electionService.isThereStartedElection(now);
        if (user != null && user.getPassword().equals(password)){
            controller = "Logged-in";
        }
        try {
                if((controller.equals("Logged-in"))){
                    if(user.getRole().equals("student")) { //  login response for student
                        // Http status 2**
                        Student student = studentService.findByUser_Email(email);
                        return new ResponseEntity<>(new LoginResponse(200, controller, student,isElectionStarted), HttpStatus.OK);
                    }
                    else if(user.getRole().equals("candidate")){ //  login response for candidate
                        Student student = studentService.findByUser_Email(email);
                        Candidate candidate = candidateService.findByStudent_StudentNumber(student.getStudentNumber());
                        return new ResponseEntity<>(new LoginResponse(200, controller, candidate,isElectionStarted), HttpStatus.OK);
                    }
                    else if(user.getRole().equals("officer")){ //  login response for candidate
                        Admin officer = adminService.findByUser_Email(email);
                        return new ResponseEntity<>(new LoginResponse(200, controller, officer,isElectionStarted), HttpStatus.OK);
                    }

                    else if(user.getRole().equals("rector")){ //  login response for candidate
                        Admin rector = adminService.findByUser_Email(email);
                        return new ResponseEntity<>(new LoginResponse(200, controller, rector,isElectionStarted), HttpStatus.OK);
                    }
            }
            else {
                // Http status 4**
                return new ResponseEntity<>(new LoginResponse(400, "Invalid Requests"), HttpStatus.BAD_REQUEST);
            }
        }
        catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new LoginResponse(400, "Email does not exists!"), HttpStatus.BAD_REQUEST);
        }
        return null;
    }



    @GetMapping("/isInElectionProcess") // checks whether in election process or not
    public boolean checkElectionInitialization() {
        LocalDateTime now = LocalDateTime.now();
        Election election = electionService.findByIsFinished(false);
        if (electionService.isThereStartedElection(now)) {
            return true; // we are in election
        } else if (election != null) {
            if (!electionService.isThereStartedElection(now) && electionService.findByIsFinished(false).getEndDate().isBefore(now)) {//  if election finished...
                for (Department department : departmentRepo.findAll()) {
                    List<Candidate> candidateList = candidateService.findCandidateByDepartmentId(department.getDepartmentId(),false); // candidate of current election.
                    if(candidateList.size() != 0){
                        List<Integer> voteList = new ArrayList<Integer>();
                        Long max = Long.valueOf(0);
                        for (Candidate candidate : candidateList) {
                            voteList.add(candidate.getVotes().intValue());
                            if (candidate.getVotes() >= max) {
                                max = candidate.getVotes();
                            }
                        }
                        int maxController = Collections.max(voteList);
                        List<Candidate> candidate = candidateService.findByVotes(Long.valueOf(maxController),department.getDepartmentId());
                        Long delegateId = Long.valueOf(delegateService.getAllDelegates().size() + 1);
                        boolean istiedaa = Collections.frequency(voteList,maxController) >= 2;
                        if(!istiedaa){ //  there is no tie.
                            User user = candidate.get(0).getStudent().getUser();
                            user.setRole("representative");
                            userService.save(user);// role is setted as representative
                            //  candidate ,user and student  saved the changes.
                            Delegate delegate = new Delegate(delegateId, candidate.get(0),true); // new delegate has been created.
                            delegateService.save(delegate);
                            // added representative to list.
                        }
                        else{
                            for(Candidate candidateTied: candidate){
                                    Delegate delegate = new Delegate(delegateId,candidateTied,null);
                                    delegateService.save(delegate);
                                    delegateId++;
                            }
                        }
                    }

                }
                Election tempElection = electionService.findByIsFinished(false);
                tempElection.setFinished(true);
                electionService.save(tempElection);
                return false; //  returns false and updates database.
            }
        }
        return false; // if we are not in election
    }
    @GetMapping("/allDelegates")
    public List<Delegate> getAllDelegates(){
        return delegateService.findByIsConfirmed(true);
    }

    @GetMapping("/tiedDelegates")
    public List<Delegate> getTiedDelegates(){
        return delegateService.findByIsConfirmed(null);// candidates that are not confirmed yet.
    }

    @GetMapping("/isInCandidacyProcess")
    public boolean checkCandidacyProcess(){
        LocalDateTime now = LocalDateTime.now();
        Election election = electionService.findByIsFinished(false); // not finished election
        if(election!= null){
            return now.isBefore(election.getStartDate());
        }
       return false; // election is not set by rector
    }



}
