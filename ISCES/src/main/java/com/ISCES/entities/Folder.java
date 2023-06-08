package com.ISCES.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@Table(name = "folder")
public class Folder{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long folderId;

    @OneToOne
    @JoinColumn(name = "student_number")
    private Student student;

    @Column(name="folder_directory")
    private String folderDirectory;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<File> files = new ArrayList<>();





}
