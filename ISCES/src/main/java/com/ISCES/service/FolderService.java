package com.ISCES.service;

import com.ISCES.entities.Folder;
import com.ISCES.repository.FolderRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
public class FolderService {
    private FolderRepo folderRepo;

    public FolderService(FolderRepo folderRepo) {
        this.folderRepo = folderRepo;
    }


    public List<Folder> findByStudent_StudentNumber(Long studentNumber) {
        return folderRepo.findByStudent_StudentNumber(studentNumber);
    }
}
