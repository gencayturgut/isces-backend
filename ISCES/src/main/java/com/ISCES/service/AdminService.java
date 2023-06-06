package com.ISCES.service;


import com.ISCES.entities.Admin;
import com.ISCES.repository.AdminRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class AdminService {
    private AdminRepo adminRepo;

    @Autowired
    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Admin findByUser_Email(String email){
        return adminRepo.findByUser_Email(email);
    }

    public Admin findByDepartmentId(Long departmentId){
        return adminRepo.findByDepartment_DepartmentId(departmentId);
    }


}
