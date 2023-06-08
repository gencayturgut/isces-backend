package com.ISCES.service;

import com.ISCES.entities.Candidate;
import com.ISCES.entities.Delegate;
import com.ISCES.entities.Department;
import com.ISCES.repository.DelegateRepo;
import com.ISCES.repository.DepartmentRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class DelegateService {
    private DelegateRepo delegateRepo;
    private CandidateService candidateService;
    private DepartmentRepo departmentRepo;

    @Autowired
    public DelegateService(DepartmentRepo departmentRepo, DelegateRepo delegateRepo, CandidateService candidateService) {
        this.delegateRepo = delegateRepo;
        this.candidateService = candidateService;
        this.departmentRepo = departmentRepo;
    }
    @Transactional
    public void delete(Delegate delegate){
        delegateRepo.delete(delegate);
    }


    @Transactional
    public void save(Delegate delegate){
        delegateRepo.save(delegate);
    }

    @Transactional
    public void deleteAll(){
        delegateRepo.deleteAll();
    }

    @Transactional
    public List<Delegate> findByIsConfirmed(Boolean isConfirmed){
        return delegateRepo.findByIsConfirmed(isConfirmed);
    }

    @Transactional
    public Delegate findByDelegateId(Long delegateId){
        return delegateRepo.findByDelegateId(delegateId);
    }

    @Transactional
    public List<Delegate> findUnconfirmedCandidatesByDepartmentIdAndIsConfirmed(Long departmentId){
        return delegateRepo.findByCandidate_Student_Department_DepartmentIdAndIsConfirmed(departmentId,null);
    }

    public List<Delegate> getAllDelegates(){
        return delegateRepo.findAll();
    }
}
