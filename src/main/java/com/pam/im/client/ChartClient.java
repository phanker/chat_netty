package com.pam.im.client;


import com.pam.im.handler.LoginResponseMessageHandler;
import com.pam.im.message.LoginRequestMessage;
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

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

// 聊天室客户端
@Slf4j
public class ChartClient {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        final MessageCodec messageCodec = new MessageCodec();
        CountDownLatch countDownLatch = new CountDownLatch(1);

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
                                    super.channelRead(ctx, msg);
//                                    if()
//                                    log.info("{}", msg);

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
//                                        try {
//                                            countDownLatch.await();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
                                    }, "system in .").run();
                                }

                            })
                            .addLast(new LoginResponseMessageHandler())
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
