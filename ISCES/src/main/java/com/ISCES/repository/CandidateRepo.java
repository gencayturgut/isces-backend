package com.ISCES.repository;


import com.ISCES.entities.Candidate;
import com.ISCES.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepo extends JpaRepository<Candidate,Long> {
    Candidate findByCandidateId(Long candidateId);
    List<Candidate> findByStudent_Department_DepartmentIdAndElection_IsFinished(Long departmentId, Boolean isFinished);

    Candidate findByStudent_StudentNumber(Long studentNumber);

    List<Candidate> findByVotesAndElection_IsFinishedAndStudent_Department_DepartmentId(Long votes,Boolean isFinished,Long departmentId);

    List<Candidate> findByElection_ElectionIdAndStudent_Department_DepartmentId(Long electionId,Long departmentId);

    List<Candidate> findByElection_ElectionId(Long electionId);

}
