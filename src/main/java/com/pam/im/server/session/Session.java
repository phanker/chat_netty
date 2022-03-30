package com.pam.im.server.session;

import io.netty.channel.Channel;

/**
 * 会话管理接口
 */
public interface Session {
    /**
     * 绑定会话
     */
    void bind(Channel channel,String username);

    /**
     * 解绑会话
     */
    void unbind(Channel channel);

    /**
     * 根据用户名获取channel
     * @param username
     * @return
     */
    Channel getChannel(String username);

}
