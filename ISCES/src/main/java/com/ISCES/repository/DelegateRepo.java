package com.ISCES.repository;

import com.ISCES.entities.Delegate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DelegateRepo extends JpaRepository<Delegate,Long> {
    List<Delegate> findByIsConfirmed(Boolean isConfirmed);
    Delegate findByDelegateId(Long delegateId);

    List<Delegate> findByCandidate_Student_Department_DepartmentIdAndIsConfirmed(Long departmentId, Boolean isConfirmed);


}
