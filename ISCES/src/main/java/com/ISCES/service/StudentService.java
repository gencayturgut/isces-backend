package com.ISCES.service;



import com.ISCES.entities.Student;
import com.ISCES.repository.FileRepo;
import com.ISCES.repository.FolderRepo;
import com.ISCES.repository.StudentRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Getter
@Setter
public class StudentService {
    private StudentRepo studentRepo;
    private FolderRepo  folderRepo;
    private FileRepo fileRepo;

    @Autowired
    public StudentService(StudentRepo studentRepo, FolderRepo folderRepo, FileRepo fileRepo) {
        this.studentRepo = studentRepo;
        this.folderRepo = folderRepo;
        this.fileRepo = fileRepo;
    }

    @Transactional
    public List<Student> getAllStudents(){
        return studentRepo.findAll();
    }

    @Transactional
    public Student findByStudentNumber(Long studentNumber){
        return studentRepo.findByStudentNumber(studentNumber);
    }


    @Transactional
    public Student save(Student student){
        return studentRepo.save(student);
    }

    @Transactional
    public  List<Student> findByDepartmentIdAndIsAppliedForCandidacyAndUser_Role(Long departmentId, Boolean isAppliedForCandidacy,String role){
        return studentRepo.findByDepartment_DepartmentIdAndIsAppliedForCandidacyAndUser_Role(departmentId,isAppliedForCandidacy,role);
    }

    @Transactional
    public Student findByUser_Email(String email){
       return studentRepo.findByUser_Email(email);
    }




}
