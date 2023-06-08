package com.ISCES.service;


import com.ISCES.entities.Candidate;
import com.ISCES.repository.CandidateRepo;
import com.ISCES.repository.ElectionRepo;
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
    private ElectionRepo electionRepo;

    @Autowired
    public CandidateService(CandidateRepo candidateRepo, ElectionRepo electionRepo) {
        this.candidateRepo = candidateRepo;
        this.electionRepo = electionRepo;
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
    public List<Candidate> findCandidateByDepartmentId(Long departmentId, Boolean isFinished){
        return candidateRepo.findByStudent_Department_DepartmentIdAndElection_IsFinished(departmentId, isFinished);
    }


    @Transactional
    public Candidate findByStudent_StudentNumber(Long studentNumber){
        return candidateRepo.findByStudent_StudentNumber(studentNumber);
    }

    public void deleteCandidate(Candidate candidate){
        candidateRepo.delete(candidate);
    }

    @Transactional
    public List<Candidate> findByVotes(Long votes,Long departmentId){
        return candidateRepo.findByVotesAndElection_IsFinishedAndStudent_Department_DepartmentId(votes,false,departmentId);
    }

    @Transactional
    public void deleteAll(){
        candidateRepo.deleteAll();
    }


    @Transactional
    public List<Candidate> findPreviousElectionCandidates(Long departmentId){
        Long lastElectionId = Long.valueOf(electionRepo.findAll().size());
        if(lastElectionId == 0){
            return null;
        }
        System.out.println(candidateRepo.findAll().get(0).getElection().getElectionId());
        System.out.println(candidateRepo.findAll().get(0).getStudent().getDepartment().getDepartmentId());
        System.out.println(candidateRepo.findByElection_ElectionIdAndStudent_Department_DepartmentId(lastElectionId,departmentId));
        return candidateRepo.findByElection_ElectionIdAndStudent_Department_DepartmentId(lastElectionId,departmentId);
    }

    @Transactional
    public List<Candidate> findByElectionId(Long electionId){
        return candidateRepo.findByElection_ElectionId(electionId);
    }



}
