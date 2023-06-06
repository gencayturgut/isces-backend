package com.ISCES.repository;

import com.ISCES.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepo extends JpaRepository<Folder, Long> {
}
