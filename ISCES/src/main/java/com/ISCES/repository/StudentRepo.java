package com.ISCES.repository;


import com.ISCES.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student,Long> {
    Student findByStudentNumber(Long studentNumber);
    List<Student> findByDepartmentIdAndIsAppliedForCandidacyAndUser_Role(Long departmentId, Boolean isAppliedForCandidacy,String role);
    List<Student> findByIsAppliedForCandidacy(Boolean isAppliedForCandidacy); //  I didnt use this.If it is neccesary for officer we can use .
    Student findByUser_Email(String email);

}
