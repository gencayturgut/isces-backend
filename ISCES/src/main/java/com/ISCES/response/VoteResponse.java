package com.ISCES.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class VoteResponse { // we can change it .

    private String message;
    private Long studentNumber;

    public VoteResponse(String message, Long studentNumber) {
        this.message = message;
        this.studentNumber = studentNumber;
    }

    public VoteResponse(String message) {
        this.message = message;
    }
}
