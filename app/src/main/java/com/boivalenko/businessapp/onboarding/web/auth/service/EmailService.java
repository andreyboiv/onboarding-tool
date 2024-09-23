package com.boivalenko.businessapp.onboarding.web.auth.service;

//Es werden verschiedene E-Mails an Employee versendet.
//E-Mails werden async versendet. @Async. @EnableAsync wurde in SpringConfig hinzugefügt.

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

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

            String htmlMsg = String.format(
                    "Hey,<br/><br/>" +
                            "<b>falls Du jetzt das erste Mal vom \"Onboarding Tool\" hörst, dann ignoriere bitte diese E-mail. <p/>" +
                            "Du brauchst das gar nicht weiter lesen! Jemand anderer hat deine E-mail anscheint (fehlerhaft) eingegeben... </b><p/><p/>" +

                            "ANSONSTEN hast Du alles richtig gemacht! :-) Und Du hast ja erfolgreich ein Account für das \"Onboarding Tool\" " +
                            "erstellt. <p/><p/>" +
                            "Dein Name bzw. Login im System lautet:<b> %s </b> <p/>Dein Passwort musstest du dir selbst merken oder irgendwo abspeichern können. <p/>" +
                            "Dabei hast du die Möglichkeit , jeder Zeit dein Passwort zurückzusetzen. <p/><p/>" +
                            "" +

                            "<b>WAS DIR NOCH FEHLT... GANZ WICHTIG!!! Für die Aktivierung deines Accounts musstest " +
                            "<a href='%s'>%s</a> klicken</b><br/><br/>" +
                            "Viel Spaß beim Onboarding <br/> Dein Onboarding Team",  username, url, "den Link.");

            return this.sendMessage(email, mimeMessage, message, htmlMsg, "Registrierung deines Accounts beim \"Onboarding Tool\" war erfolgreich");

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

            String htmlMsg = String.format("Hey,<br/><br/> <b> falls du keine Passwort Änderung " +
                    "für Dein Account beim \"Onboarding Tool\" wünschst, <p/>" +
                    "kannst du diese E-mail SICHER ignorieren. </b> <p/><p/>" +
                    "Ansonsten klick bitte den folgenden Link innerhalb %d min, um den Passwort ändern zu können. <br/><br/>" +
                    "<a href='%s'>%s</a>", this.resetPasswordTokenExpiration/1000/60, url, "Passwort zurücksetzen");

            htmlMsg = htmlMsg + "<br/><br/>Viel Spaß beim Onboarding <br/> Dein Onboarding Team";

            return this.sendMessage(email, mimeMessage, message, htmlMsg, "Passwort zurücksetzen beim Onboarding");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }

    //Eine "Hallo" E-Mail wird geschickt.
    @Async
    public Future<Boolean> sendAktivierungsEmail(String email, String login){
        try {
            MimeMessage mimeMessage = sender.createMimeMessage(); // es wird nicht eine TXT Datei erzeugt, sondern HTML
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = String.format("Hey <b>%s</b><br/><br/>Wir freuen uns echt riesig, Dich beim Team begrüßen zu dürfen! <p/><p/>" +
                    "Dein Onboarding Account ist nun ja aktiviert und Du kannst dich gerne einloggen/ausloggen. <p/> Und natürlich kannst du bitte gerne Deine Tasks machen... ;-)", login);

            htmlMsg = htmlMsg + "<br/><br/>Viel Spaß beim Onboarding <p/><p/> Dein Onboarding Team";

            return this.sendMessage(email, mimeMessage, message, htmlMsg, "Dein Onboarding Account ist aktiviert");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }
    @Async
    public Future<Boolean> sendDeaktivierungsEmail(String email, String login) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage(); // es wird nicht eine TXT Datei erzeugt, sondern HTML
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = String.format("Hey <b>%s </b>, <br/><br/>" +
                    "dein Account beim Onboarding ist deaktiviert. <p/> Der Grund: Du hast ja alle Tasks erledigt markiert und brauchst kein Onboarding mehr." +
                    " <p/><p/><b>Wenn Du doch was nachholen musst, wende dich bitte an jemanden vom Onboarding Team, dass du den Zugang wieder bekommst.</b>", login);

            htmlMsg = htmlMsg + "<br/><br/>Tschau & Viel Erfolg und Spaß bei Kunden Projekten <p/><p/> Dein Onboarding Team";

            return this.sendMessage(email, mimeMessage, message, htmlMsg, "Account Deaktivierung beim Onboarding Tool");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }

    @Async
    public Future<Boolean> sendPasswordGeandertEmail(String email, String login) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage(); // es wird nicht eine TXT Datei erzeugt, sondern HTML
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = String.format("Hey <b>%s </b>. <p/><p/><p/><p/>Dein Password wurde erfolgreich geändert. <p/><p/><p/><p/>" +
                    "<p/><p/> <b>Wende dich bitte zügig an Administrator, falls Du Dein Password doch nicht geändert hast.</b> <p/><p/>", login);

            htmlMsg = htmlMsg + "<br/><br/>Viel Spaß bei Onboarding <p/><p/> Onboarding Team";

            return this.sendMessage(email, mimeMessage, message, htmlMsg, "Password erfolgreich geändert");

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


    @Async
    public Future<Boolean> sendDeaktivierungsEmailtoAdmin(String email, String login) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage(); // es wird nicht eine TXT Datei erzeugt, sondern HTML
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = String.format("Es geht um Onboarding von <b>%s </b> . Er/sie hat alle Tasks erledigt", login);

            return this.sendMessage(this.emailFrom, mimeMessage, message, htmlMsg, "Account Deaktivierung für " + login);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }
}
