package com.pam.im.server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {
    private final Map<String,Channel> usernameChannelMap = new ConcurrentHashMap();

    private final Map<Channel,String> channelUsernameMap = new ConcurrentHashMap();
    private final Map<Channel,Map<String,Object>> channelAttributesMap = new ConcurrentHashMap();

    public void bind(Channel channel, String username) {
        usernameChannelMap.put(username,channel);
        channelUsernameMap.put(channel,username);
        channelAttributesMap.put(channel,new ConcurrentHashMap());
    }

    public void unbind(Channel channel) {
        String remove = channelUsernameMap.remove(channel);
        usernameChannelMap.remove(remove);
        channelAttributesMap.remove(channel);
    }

    public Channel getChannel(String username) {
        return usernameChannelMap.get(username);
    }
}
