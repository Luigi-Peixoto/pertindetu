package com.ufrn.pertindetu.base.utils.email;

import com.ufrn.pertindetu.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendPasswordReset(User user, String resetUrl) {
        // In development we just log the reset URL. Replace with real email sending if needed.
        logger.info("Password reset requested for user {}. Reset URL: {}", user.getEmail(), resetUrl);
    }
}

