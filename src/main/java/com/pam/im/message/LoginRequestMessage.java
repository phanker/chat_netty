package com.pam.im.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestMessage extends Message{
    private String username;
    private String password;
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
