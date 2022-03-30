package com.pam.im.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

@Slf4j
public class HttpProtocolServer {

    public static void main(String[] args)  {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            serverBootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.DEBUG))
                                    .addLast(new HttpServerCodec()) //http的解码器
//                                    .addLast(new ChannelInboundHandlerAdapter(){
//                                        @Override
//                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { //触发读事件
//                                            log.info("{}",msg.getClass());
////                                            content.
////                                            DefaultHttpResponse defaultHttpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
////                                            ctx.writeAndFlush("hello world".getBytes());
//                                        }
//                                    })
                                    .addLast(new SimpleChannelInboundHandler<io.netty.handler.codec.http.HttpRequest>() {
                                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                            String uri = msg.getUri();
                                            HttpMethod method = msg.getMethod();
                                            log.info("uri:{},method:{}",uri,method);
                                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(),HttpResponseStatus.OK);
                                            String s = " <h1>hello world !!</h1>";

                                            response.headers().setInt(CONTENT_LENGTH,s.length());
                                            response.content().writeBytes(s.getBytes());
                                            ctx.writeAndFlush(response);
                                        } //只对具体的事件类型感兴趣
                                    });
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(8080)
                    .sync();

            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();//优雅关闭
        }


    }
}
