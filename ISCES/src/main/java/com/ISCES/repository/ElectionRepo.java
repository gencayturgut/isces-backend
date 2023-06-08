package com.ISCES.repository;

import com.ISCES.entities.Election;
import com.ISCES.request.ElectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectionRepo extends JpaRepository<Election, Long> {
    Election findByIsFinished(boolean isFinished);
    Election findByElectionId(Long electionId);

}
