package br.com.fatecads.fatecads.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "mail.mode", havingValue = "console", matchIfMissing = true)
public class ConsoleMailService implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleMailService.class);

    @Override
    public void sendPasswordReset(String toEmail, String resetLink) {
        logger.info("Password reset link for {}: {}", toEmail, resetLink);
    }
}
