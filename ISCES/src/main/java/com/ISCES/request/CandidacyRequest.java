package com.ISCES.request;

import com.ISCES.entities.Candidate;
import com.ISCES.entities.Student;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Data
public class CandidacyRequest { // it can change
    private String message;
    private Long studentNumber;
    MultipartFile[] files;
    public CandidacyRequest(String message) {
        this.message = message;
    }

    public CandidacyRequest(Long studentNumber, String message) {
        this.studentNumber = studentNumber;
        this.message = message;
    }

    public CandidacyRequest(Long studentNumber, MultipartFile[] files) {
        this.message = message;
        this.studentNumber = studentNumber;
        this.files = files;
    }
}
