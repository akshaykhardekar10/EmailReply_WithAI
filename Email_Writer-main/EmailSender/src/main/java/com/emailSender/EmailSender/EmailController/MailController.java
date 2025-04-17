package com.emailSender.EmailSender.EmailController;

import com.emailSender.EmailSender.EmailDTO.EmailDTO;
import com.emailSender.EmailSender.EmailService.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
@CrossOrigin(origins = "*")
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> createMail(@RequestBody EmailDTO body){
        String response = emailService.generateEmail(body);
        return ResponseEntity.ok(response);
    }
}
