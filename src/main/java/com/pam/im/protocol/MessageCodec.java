package com.pam.im.protocol;

import com.pam.im.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf,Message> {

    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception {
        ByteBuf out = ctx.alloc().buffer(); //声明一个byteBuf 空间
        /**
         * 字节的魔数
         */
        out.writeBytes(new byte[]{1,2,3,4});
        //字节的版本
        out.writeByte(1);
        //字节的序列化方式 0 jdk 1 json
        out.writeByte(0);
        //消息的类型
        out.writeByte(msg.getMessageType());
        // 4个字节 消息序号
        out.writeInt(msg.getSequenceId());
        //无意义 对其补充
        out.writeByte(0xff);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        //长度
        out.writeInt(bytes.length);
        //写入内容
        out.writeBytes(bytes);
        list.add(out);
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt(); // 魔数
        byte version = in.readByte();//版本号
        byte serializeType = in.readByte();//序列号方式
        byte messageType = in.readByte(); //消息类型
        int dequenceId = in.readInt();//消息序号
        in.readByte(); //无意义的填充
        int messageLength = in.readInt(); //消息长度
        byte[] bytes = new byte[messageLength]; //消息内容
        in.readBytes(bytes,0,messageLength);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream oos = new ObjectInputStream(bis);
        Message message = (Message)oos.readObject();
        log.info("{},{},{},{},{},{},{}",magicNum,version,serializeType,messageType,dequenceId,messageLength);
        log.info("{}",message);
        out.add(message);
    }
}
