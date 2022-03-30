package com.pam.im.handler;

import com.pam.im.message.LoginResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class LoginResponseMessageHandler extends SimpleChannelInboundHandler<LoginResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponseMessage msg) throws Exception {
        String reason = msg.getReason();
        Boolean success = msg.getSuccess();
        log.info("success:{},reason:{}",success,reason);
    }
}
