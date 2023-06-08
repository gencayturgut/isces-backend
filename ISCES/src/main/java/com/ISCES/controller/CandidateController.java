package com.ISCES.controller;


import com.ISCES.entities.Candidate;
import com.ISCES.service.CandidateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("https://iztechelectionfrontend.herokuapp.com/")
public class CandidateController { // Bütün return typeler değişebilir . Response ve Request packageına yeni classlar eklenmeli frontendden hangi bilgi istendiğine göre
    CandidateService candidateService;
    public CandidateController(CandidateService candidateService){
        this.candidateService = candidateService;
    }

    @GetMapping("/candidates/allCandidates/{departmentId}")
    public List<Candidate> getAllCurrentCandidates(@PathVariable Long departmentId){  // candidates of current election candidates
        return candidateService.findCandidateByDepartmentId(departmentId,false);
    }



    @GetMapping("/candidates/allPreviousElectionCandidates/{departmentId}")
    public List<Candidate> getAllPreviousElectionCandidates(@PathVariable Long departmentId){// candidates of previous election.
        return candidateService.findPreviousElectionCandidates(departmentId);
    }


}
