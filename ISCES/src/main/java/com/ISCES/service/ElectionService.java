package com.ISCES.service;

import com.ISCES.entities.Election;
import com.ISCES.repository.ElectionRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Service
public class ElectionService {
    private ElectionRepo electionRepo;

    @Autowired
    public ElectionService(ElectionRepo electionRepo) {
        this.electionRepo = electionRepo;
    }


    @Transactional
    public Election save(Election election){
        return electionRepo.save(election);
    }

    @Transactional
    public List<Election> getAllElections(){
        return electionRepo.findAll();
    }

    @Transactional
    public Election findByIsFinished(boolean isFinished){
        return electionRepo.findByIsFinished(isFinished);
    }



    @Transactional
    public boolean isThereStartedElection(LocalDateTime currentTime) {
        Election election = electionRepo.findByIsFinished(false); // a not finished election
        if (election == null) {
            return false;
        }
        return  election.getStartDate().isBefore(currentTime) && election.getEndDate().isAfter(currentTime); // if times are okay.
    } //  checks for is there any election that initialize by rector.

    @Transactional
    public boolean isEnteredElectionDateByRector(){
        Election election = electionRepo.findByIsFinished(false);
        return election != null; // checks is there  entered election
    }

    public Election findByElectionId(Long electionId) {
        return electionRepo.findByElectionId(electionId);
    }
}
