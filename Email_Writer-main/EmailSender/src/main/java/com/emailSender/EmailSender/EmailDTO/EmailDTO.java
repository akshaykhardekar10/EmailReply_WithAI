package com.emailSender.EmailSender.EmailDTO;

import lombok.Data;

@Data
public class EmailDTO {

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    private String emailContent;
    private String tone;
}
