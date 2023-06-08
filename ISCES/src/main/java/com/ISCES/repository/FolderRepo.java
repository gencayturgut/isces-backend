package com.ISCES.repository;

import com.ISCES.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepo extends JpaRepository<Folder, Long> {
    List<Folder> findByStudent_StudentNumber(Long studentNumber);
}
