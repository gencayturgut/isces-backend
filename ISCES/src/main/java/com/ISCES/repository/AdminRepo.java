package com.ISCES.repository;

import com.ISCES.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<Admin,Long> {
    Admin findByUser_Email(String email);
    Admin findByDepartmentId(Long departmentId);
}
