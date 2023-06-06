package com.ISCES.service;


import com.ISCES.entities.Candidate;
import com.ISCES.repository.CandidateRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Setter
@Service
public class CandidateService {
    private CandidateRepo candidateRepo;

    @Autowired
    public CandidateService(CandidateRepo candidateRepo) {
        this.candidateRepo = candidateRepo;
    }

    public List<Candidate> getAllCandidates(){
        return candidateRepo.findAll();
    }

    @Transactional
    public Candidate findById(Long candidateId){
        return candidateRepo.findByCandidateId(candidateId);
    }



    @Transactional
    public Candidate save(Candidate candidate){
        return candidateRepo.save(candidate);
    }

    @Transactional
        public void getVote(Candidate  candidate){
        candidate.setVotes(candidate.getVotes() + 1);
        candidateRepo.save(candidate);
    }


    @Transactional
    public List<Candidate> findCandidateByDepartmentId(Long departmentId){
        return candidateRepo.findByStudent_Department_DepartmentId(departmentId);
    }


    @Transactional
    public Candidate findByStudent_StudentNumber(Long studentNumber){
        return candidateRepo.findByStudent_StudentNumber(studentNumber);
    }

    public void deleteCandidate(Candidate candidate){
        candidateRepo.delete(candidate);
    }

    @Transactional
    public Candidate findByVotes(Long votes){
        return candidateRepo.findByVotes(votes);
    }

    @Transactional
    public void deleteAll(){
        candidateRepo.deleteAll();
    }

}
