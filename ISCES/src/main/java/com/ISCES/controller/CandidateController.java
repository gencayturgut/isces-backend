package com.ISCES.controller;


import com.ISCES.entities.Candidate;
import com.ISCES.entities.Election;
import com.ISCES.service.CandidateService;
import com.ISCES.service.ElectionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("https://iztechelectionfrontend.herokuapp.com")
public class CandidateController { // Bütün return typeler değişebilir . Response ve Request packageına yeni classlar eklenmeli frontendden hangi bilgi istendiğine göre
    CandidateService candidateService;
    ElectionService electionService;
    public CandidateController(CandidateService candidateService, ElectionService electionService){
        this.candidateService = candidateService;
        this.electionService = electionService;
    }

    @GetMapping("/candidates/allCandidates/{departmentId}")
    public List<Candidate> getAllCurrentCandidates(@PathVariable Long departmentId){  // candidates of current election candidates
        return candidateService.findCandidateByDepartmentId(departmentId,false);
    }

    @GetMapping("/candidates/allCandidates")
    public List<Candidate> getAllCurrentElectionCandidates(){
        Election election = electionService.findByIsFinished(false); // not finished election
        System.out.println(election);
        return candidateService.findByElectionId(election.getElectionId());
    }



    @GetMapping("/candidates/allPreviousElectionCandidates/{departmentId}")
    public List<Candidate> getAllPreviousElectionCandidates(@PathVariable Long departmentId){// candidates of previous election.
        return candidateService.findPreviousElectionCandidates(departmentId);
    }


}
