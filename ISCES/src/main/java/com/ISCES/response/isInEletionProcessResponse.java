package com.ISCES.response;

import com.ISCES.entities.Delegate;

import java.util.List;

public class isInEletionProcessResponse {
    private boolean isInElection;
    private List<Delegate> delegateList;

    private String message;

    public isInEletionProcessResponse(boolean isInElection, List<Delegate> delegateList) {
        this.isInElection = isInElection;
        this.delegateList = delegateList;
    }

    public isInEletionProcessResponse(boolean isInElection) {
        this.isInElection = isInElection;
    }

    public isInEletionProcessResponse(String message) {
        this.message = message;
    }
}
