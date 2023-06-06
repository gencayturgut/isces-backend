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
    public List<Delegate> getAllDelegates() {
        return delegateRepo.findAll();
    }

    @Transactional
    public void save(Delegate delegate){
        delegateRepo.save(delegate);
    }

    @Transactional
    public void deleteAll(){
        delegateRepo.deleteAll();
    }
}
