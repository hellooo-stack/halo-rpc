package site.hellooo.rpc.server;

public class DefaultMessage implements IMessage {
    private byte version;
    private byte type;
    private byte codec;
    private int requestId;
    private int bodyLength;
    private byte[] content;

    @Override
    public byte getVersion() {
        return 0;
    }

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public byte getCodec() {
        return 0;
    }

    @Override
    public int getRequestId() {
        return 0;
    }

    @Override
    public int getBodyLength() {
        return 0;
    }

    @Override
    public byte[] getContent() {
        return new byte[0];
    }
}
