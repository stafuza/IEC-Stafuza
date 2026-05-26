package br.com.fatecads.fatecads.service;

public interface MailService {
    void sendPasswordReset(String toEmail, String resetLink);
}
