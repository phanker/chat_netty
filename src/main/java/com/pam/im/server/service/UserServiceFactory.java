package com.pam.im.server.service;

public class UserServiceFactory {

    private static UserService userService;
    static{
        userService = new UserServiceImpl();
    }

    public static UserService getUserService(){
        return userService;
    }
}
