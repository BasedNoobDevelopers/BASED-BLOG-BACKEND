package com.noobsmoke.basedblogbackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String subject, String text) {
        try {
            MimeMessage emailMessage = mailSender.createMimeMessage();
            MimeMessageHelper emailMessageHelper = new MimeMessageHelper(emailMessage, true);

            emailMessageHelper.setTo(to);
            emailMessageHelper.setSubject(subject);
            emailMessageHelper.setText(text, true);

            mailSender.send(emailMessage);
        } catch (MessagingException messagingException) {
            throw new RuntimeException("Email Not Sent " + messagingException.getMessage());
        }
    }

    public String buildVerificationEmailHtml(String verificationCode) {
        return "<!doctype html>"
                    + "<html lang=\"en\">"
                    + "<head><meta charset=\"UTF-8\" /><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\" /></head>"

                    + "<body style=\"margin:0;padding:0;background-color:#0f172a;\">"

                    + "<div style=\"display:none;max-height:0;overflow:hidden;opacity:0;color:transparent;\">"
                    + "Your mission code is " + verificationCode + ". It expires in 10 minutes."
                    + "</div>"

                    + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#0f172a;\">"
                    + "<tr><td align=\"center\" style=\"padding:24px 12px;\">"

                    + "<table role=\"presentation\" width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;max-width:600px;background:#020617;border-radius:16px;overflow:hidden;border:1px solid #1e293b;\">"

                    + "<tr><td style=\"padding:22px 24px;background:#020617;border-bottom:1px solid #1e293b;\">"
                    + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:18px;color:#22c55e;font-weight:700;\">Young Based Blog</div>"
                    + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px;color:#64748b;margin-top:6px;\">MISSION: EMAIL VERIFICATION</div>"
                    + "</td></tr>"

                    + "<tr><td>"
                    + "<img src=\"https://media.tenor.com/M9LiZpiqCe0AAAAM/batman-arkham.gif\" "
                    + "alt=\"Verification\" style=\"width:100%;max-width:600px;display:block;border:0;opacity:0.9;\"/>"
                    + "</td></tr>"

                    + "<tr><td style=\"padding:28px 24px;\">"

                    + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:16px;color:#e2e8f0;font-weight:700;\">Player Authentication Required</div>"

                    + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:14px;color:#94a3b8;margin-top:10px;\">"
                    + "To continue your mission, enter the verification code below."
                    + "</div>"

                    + "<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:20px 0;\">"
                    + "<tr><td style=\"background:#020617;border:2px solid #22c55e;border-radius:12px;padding:16px 20px;box-shadow:0 0 10px rgba(34,197,94,0.4);\">"

                    + "<div style=\"font-family:Courier,monospace;font-size:30px;letter-spacing:8px;color:#22c55e;font-weight:800;text-align:center;\">"
                    + verificationCode
                    + "</div>"

                    + "</td></tr></table>"

                    + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px;color:#64748b;\">"
                    + "Code expires in 10 minutes."
                    + "</div>"

                    + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px;color:#475569;margin-top:8px;\">"
                    + "If you did not initiate this request, abort mission."
                    + "</div>"

                    + "</td></tr>"

                    + "<tr><td style=\"padding:16px 24px;background:#020617;border-top:1px solid #1e293b;text-align:center;\">"
                    + "<div style=\"font-family:Arial,Helvetica,sans-serif;font-size:11px;color:#334155;\">"
                    + "SYSTEM MESSAGE • DO NOT REPLY"
                    + "</div>"
                    + "</td></tr>"

                    + "</table></td></tr></table>"
                    + "</body></html>";
    }
}
