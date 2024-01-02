package com.boivalenko.businessapp.teamtasksplanning.web.auth.service;

//Es werden verschiedene E-Mails an Employee versendet.
//E-Mails werden async versendet. @Async. @EnableAsync wurde in SpringConfig hinzugefügt.

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;

@Service
@Log
@RequiredArgsConstructor
public class EmailService {

    // Client's URL
    @Value("${client.url}")
    private String clientURL;

    // Absender
    @Value("${email.from}")
    private String emailFrom;

    @Value("${jwt.resetPasswordTokenExpiration}")
    private int resetPasswordTokenExpiration;

    private final JavaMailSender sender;

    @Async
    public Future<Boolean> sendActivationEmail(String email, String username, String uuid) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage(); // es wird nicht eine TXT Datei erzeugt, sondern HTML
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            //URL geht zunächst an Client mit GET Request.
            //Und dann geht es vom Client an Backend mit POST Request
            // "/activate-account" ist nicht Backend entry point beim Backend Controller,
            // das ist tatsächlich ein entry point beim Frontend....
            String url = clientURL + "/activate-account/" + uuid;

            String htmlMsg = String.format("Herzlich willkommen. Sie sind als %s registriert. Klicken Sie bitte die URL, " +
                    "um ihren Account zu aktivieren:%s . Ohne Aktivieren dürfen Sie sich nicht einloggen. MfG", username, url);

            return this.sendMessage(email, mimeMessage, message, htmlMsg, "Activation erforderlich");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }
    
    @Async
    public Future<Boolean> sendResetPassword(String email, String token) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage(); // es wird nicht eine TXT Datei erzeugt, sondern HTML
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            //URL geht zunächst an Client mit GET Request.
            //Und dann geht es vom Client an Backend mit POST Request
            // "/update-password/" ist nicht Backend entry point beim Backend Controller,
            // das ist tatsächlich ein entry point beim Frontend....
            String url = clientURL + "/update-password/" + token;

            String htmlMsg = String.format("Hallo, falls Sie keine Password Änderung angefordert haben, wenden Sie sich an Admin. " +
                    "Weil es eine Gefahr für Ihren Account bestehen kann." +
                    "Ansonsten klicken Sie bitte den Link innerhalb %d min, um den Password zu ändern. URL:%s", this.resetPasswordTokenExpiration/1000/60, url);

            return this.sendMessage(email, mimeMessage, message, htmlMsg, "Abfrage für Password Änderung");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }

    private Future<Boolean> sendMessage(String email, MimeMessage mimeMessage, MimeMessageHelper message, String htmlMsg, String s) throws MessagingException {
        mimeMessage.setContent(htmlMsg, "text/html");

        message.setTo(email);
        message.setFrom(emailFrom);
        message.setSubject(s);
        message.setText(htmlMsg, true);

        sender.send(mimeMessage);

        return new AsyncResult<>(true);
    }

}
