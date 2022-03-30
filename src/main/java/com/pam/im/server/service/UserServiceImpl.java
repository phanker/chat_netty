package com.pam.im.server.service;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {

    public static Map<String,String> allUserMap = new HashMap();

    static{
        allUserMap.put("zhangsan","123");
        allUserMap.put("lisi","123");
        allUserMap.put("wangwu","123");
        allUserMap.put("zhaoliu","123");
        allUserMap.put("qianqi","123");
    }

    public boolean login(String user, String password) {
        String ps = allUserMap.get(user);
        if(ps == null) return false;
        return ps.equals(password);
    }
}