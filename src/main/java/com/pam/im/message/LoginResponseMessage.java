package com.pam.im.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseMessage extends Message{

    private Boolean success;

    private String reason;

    public int getMessageType() {
        return LoginResponseMessage;
    }
}
