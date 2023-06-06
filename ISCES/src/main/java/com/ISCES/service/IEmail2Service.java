package com.ISCES.service;

import java.time.LocalDateTime;

public interface IEmail2Service {
    String sendEmail(String to, LocalDateTime electionStartDate, LocalDateTime electionEndDate);
}
