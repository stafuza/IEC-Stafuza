package br.com.fatecads.fatecads.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "mail.mode", havingValue = "smtp")
public class SmtpMailService implements MailService {
    private final JavaMailSender mailSender;
    private final String fromAddress;

    public SmtpMailService(JavaMailSender mailSender, @Value("${mail.from}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendPasswordReset(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromAddress);
        message.setSubject("Redefinicao de senha - FatecADS");
        message.setText("Use o link para redefinir sua senha: " + resetLink);
        mailSender.send(message);
    }
}
