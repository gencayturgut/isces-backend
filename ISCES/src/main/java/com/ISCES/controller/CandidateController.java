package com.ISCES.controller;


import com.ISCES.entities.Candidate;
import com.ISCES.service.CandidateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("https://iztechelection.vercel.app/")
public class CandidateController { // Bütün return typeler değişebilir . Response ve Request packageına yeni classlar eklenmeli frontendden hangi bilgi istendiğine göre
    CandidateService candidateService;
    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }



    @GetMapping("/candidates/{departmentId}")
    public List<Candidate> getAllCandidatesByDepartmentId(@PathVariable Long departmentId){
        return candidateService.findCandidateByDepartmentId(departmentId);
    }



    @GetMapping("/candidates/allCandidates")
    public List<Candidate> getAllCandidates(){
        return candidateService.getAllCandidates();
    }


}
