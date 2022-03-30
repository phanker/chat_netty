package com.pam.im.server;

import com.pam.im.handler.LoginRequestMessageHandler;
import com.pam.im.message.LoginRequestMessage;
import com.pam.im.protocol.MessageCodec;
import com.pam.im.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

//聊天室服务端
public class ChartServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        ServerBootstrap strap = new ServerBootstrap();
        final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        final MessageCodec messageCodec = new MessageCodec(); //自定义的消息解码器
        final LoginRequestMessageHandler loginRequestMessageHandler = new LoginRequestMessageHandler();
        try {
            ChannelFuture future = strap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(loggingHandler)
                                    //最大支持1024字节的消息，从12个字节后的第4个字节可以拿到消息体的长度
                                    .addLast(new ProtocolFrameDecoder())
                                    .addLast(messageCodec)
                                    .addLast(loginRequestMessageHandler);

                        }
                    })
                    .bind(8080)
                    .sync(); //创建连接后向下执行
            Channel channel = future.channel(); //
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {//优雅关闭
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
