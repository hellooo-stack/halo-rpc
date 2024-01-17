package site.hellooo.rpc.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<DefaultMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, DefaultMessage defaultMessage, ByteBuf byteBuf) throws Exception {

    }
}
