package com.ISCES.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class Email2Service implements IEmail2Service {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendEmail(String to, LocalDateTime electionStartDate,LocalDateTime electionEndDate){ // mail for election start dates
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Format LocalDateTime to String

        try {
            String msgBody;
            String subject = "ISCES Department Representative Election";

            String formattedStartDateTime = electionStartDate.format(dateTimeFormatter);
            String formattedEndDateTime = electionEndDate.format(dateTimeFormatter);
            msgBody = "Okulumuzdaki bölüm temsilciliği seçimleri için başlangıç ve bitiş tarihleri kesinleştirilmiştir." + "\n" +
                    "Seçim başlangıç tarihi: " + electionStartDate + "\n" +
                    "Seçim bitiş tarihi: " + electionEndDate;

            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(to);
            mailMessage.setText(msgBody);
            mailMessage.setSubject(subject);

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }



}
