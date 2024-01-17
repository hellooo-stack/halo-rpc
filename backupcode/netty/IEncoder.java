package site.hellooo.rpc.server;

public interface IEncoder {
    <T> byte[] encode(T data);
}
