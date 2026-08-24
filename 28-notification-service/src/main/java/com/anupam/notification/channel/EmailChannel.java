package com.anupam.notification.channel;

import com.anupam.notification.model.NotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Email notification channel implementation using Spring Mail and Thymeleaf templates.
 * <p>
 * Renders HTML email content from named templates with variable substitution,
 * then sends the email via the configured JavaMailSender.
 * </p>
 *
 * @author Anupam
 */
@Component
public class EmailChannel implements NotificationChannel {

    private static final Logger log = LoggerFactory.getLogger(EmailChannel.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /**
     * Constructs the email channel with mail sender and template engine.
     *
     * @param mailSender     the Spring mail sender for delivering emails
     * @param templateEngine the Thymeleaf engine for rendering HTML templates
     */
    public EmailChannel(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public NotificationRequest.Channel getChannel() {
        return NotificationRequest.Channel.EMAIL;
    }

    /**
     * Sends an HTML email to the recipient using a Thymeleaf template.
     *
     * @param request the notification request with recipient, template, and variables
     * @throws RuntimeException if email delivery fails (triggers retry)
     */
    @Override
    public void send(NotificationRequest request) {
        try {
            // Render the HTML body from the Thymeleaf template
            String htmlContent = renderTemplate(request.templateName(), request.variables());

            // Build the MIME message with HTML content
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(request.recipient());
            helper.setSubject(resolveSubject(request.templateName()));
            helper.setText(htmlContent, true);
            helper.setFrom("notifications@example.com");

            mailSender.send(message);
            log.info("Email sent to: {}", request.recipient());
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", request.recipient(), e.getMessage());
            throw new RuntimeException("Email delivery failed", e);
        }
    }

    /**
     * Renders a Thymeleaf template with the given variables.
     *
     * @param templateName the template name (resolved by Thymeleaf)
     * @param variables    key-value pairs for template variable substitution
     * @return the rendered HTML string
     */
    private String renderTemplate(String templateName, Map<String, String> variables) {
        Context context = new Context();
        if (variables != null) {
            variables.forEach(context::setVariable);
        }
        return templateEngine.process(templateName, context);
    }

    /**
     * Resolves the email subject line based on the template name.
     *
     * @param templateName the notification template name
     * @return the subject line for the email
     */
    private String resolveSubject(String templateName) {
        return switch (templateName) {
            case "welcome" -> "Welcome to Our Platform!";
            case "otp" -> "Your Verification Code";
            case "password-reset" -> "Password Reset Request";
            default -> "Notification";
        };
    }
}
