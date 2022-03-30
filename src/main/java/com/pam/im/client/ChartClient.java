package com.pam.im.client;


import com.pam.im.message.LoginRequestMessage;
import com.pam.im.message.LoginResponseMessage;
import com.pam.im.protocol.MessageCodec;
import com.pam.im.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

// 聊天室客户端
@Slf4j
public class ChartClient {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        final MessageCodec messageCodec = new MessageCodec();
        final AtomicBoolean Flag = new AtomicBoolean(false);
        try {
            ChannelFuture future = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ProtocolFrameDecoder());
                            pipeline.addLast(loggingHandler);
                            pipeline.addLast(messageCodec);
                            pipeline.addLast("connect handle..", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { //监听读事件
                                    log.info("{}",msg);
                                    if(msg instanceof LoginResponseMessage){
                                        LoginResponseMessage message = (LoginResponseMessage)msg;
                                        Boolean success = message.getSuccess();
                                        String reason = message.getReason();
                                        log.info("{}",reason);
                                        if(success){
                                            Flag.set(true);
                                        }else{
                                            Flag.set(false);
                                        }
                                    }
                                    countDownLatch.countDown();
                                }
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {  //在连接建立后触发事件
                                    new Thread(() -> {
                                        Scanner scanner = new Scanner(System.in);
                                        System.out.println("请输入用户名");
                                        String username = scanner.nextLine();
                                        System.out.println("请输入密码");
                                        String password = scanner.nextLine();
                                        LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);
                                        ctx.writeAndFlush(loginRequestMessage);
                                        System.out.println("等待后续操作...");
                                        try {
                                            countDownLatch.await();
                                            boolean b = Flag.get();
                                            if(b){

                                            }else{
                                                log.info("登录失败...");
                                                return;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }, "system in .").start();


                                }



                            })
//                            .addLast(new LoginResponseMessageHandler())
                            ;
                        }
                    }).connect("localhost", 8080).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
//            group.shutdownGracefully();
        }

    }
}
