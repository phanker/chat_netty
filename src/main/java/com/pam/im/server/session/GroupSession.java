package com.pam.im.server.session;

import java.nio.channels.Channel;
import java.util.List;
import java.util.Set;

public interface GroupSession {
    /**
     * 创建聊天组
     * @param name
     * @param members
     * @return
     */
    Group createGroup(String name, Set<String> members);

    /**
     * 加入聊天组
     * @param name
     * @param username
     * @return
     */
    Group joinMember(String name,String username);

    /**
     * 移除聊天组
     * @param name
     * @return
     */
    Group removeMember(String name);


    /**
     *  获取组成员
     * @param name
     * @return
     */
    Set<String> getMembers(String name);

    /**
     *  获取组成员
     * @param name
     * @return
     */
    List<Channel> getMembersChannel(String name);
}
