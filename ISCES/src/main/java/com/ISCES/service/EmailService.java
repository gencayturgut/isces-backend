package com.ISCES.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmailService implements IEMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendEmail(String to, Boolean approve){ //  mail for candidate approval.
        try {
            String msgBody;
            String subject = "ISCES Department Representative Elections";


            if (approve){
                msgBody = "Tebrikler, bölüm temsilciliği seçimi için yaptığınız adaylık başvurusu onaylandı.";
            }else {
                msgBody = "Maalesef,  bölüm temsilciliği seçimi için yaptığınız adaylık başvurusu reddedildi.";
            }

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
