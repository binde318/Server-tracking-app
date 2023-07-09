package com.example.instantastatusapp.email;

import com.example.instantastatusapp.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Configuration
@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(EmailDto emailDto) {

        String content = "<p>Hello,</p>"
                + "<p>You have been invited to register at Instanta Status Page.</p>"
                + "<p>Click the link below to Register:</p>"
                + "<p><a href=\"" + emailDto.getBody() + "\"> Register Here</a></p>";

        log.info("inside Send email, building mail!!");
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(emailDto.getSender(),"Instanta Status App");
            mimeMessageHelper.setTo(emailDto.getRecipient());
            mimeMessageHelper.setSubject(emailDto.getSubject());
            mimeMessageHelper.setText(content,true);

        };

        try {
            CompletableFuture.runAsync(() ->
                    mailSender.send(mimeMessagePreparator)).exceptionally(exp -> {
                throw new CustomException("Exception occurred sending mail [message]: " + exp.getLocalizedMessage());
            });
            log.info("email has sent!!");
        }catch (MailException exception) {
            log.error("Exception occurred when sending mail {}",exception.getMessage());
            throw new CustomException("Exception occurred when sending mail to " + emailDto.getRecipient(), HttpStatus.EXPECTATION_FAILED);
        }

    }





//    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
//
//    private final JavaMailSender mailSender;
//
//    @Override
//    @Async
//    public void send(String to, String email,String body) {
//        try{
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
//            helper.setText(email, true);
//            helper.setTo(to);
//            helper.setSubject("Confirm your email");
//            helper.setFrom("gr8erkay@gmail.com");
//        }catch (MessagingException e) {
//                LOGGER.error("failed to send email", e);
//                throw new IllegalStateException("failed to send email");
//        }
//
//    }

//    @Override
//    public void sendEmail(String invitationUrl, String email) {
//        send(email,"",invitationUrl);
//
//    }
}
