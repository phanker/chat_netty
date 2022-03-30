package com.pam.im.handler;


import com.pam.im.message.LoginRequestMessage;
import com.pam.im.message.LoginResponseMessage;
import com.pam.im.server.service.UserServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String password = msg.getPassword();
        String username = msg.getUsername();
        LoginResponseMessage loginResponseMessage = null;
        boolean login = UserServiceFactory.getUserService().login(username, password);
        if(login){
            loginResponseMessage = new LoginResponseMessage(true,"登录成功");
        }else{
            loginResponseMessage = new LoginResponseMessage(false,"登录失败");
        }
        ctx.writeAndFlush(loginResponseMessage);
    }

}
