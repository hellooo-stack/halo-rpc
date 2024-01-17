package site.hellooo.rpc.server;

public interface ISerializer {
    <T> byte[] serialize(T t);
    <T> T deserialize(byte[] bytes);
}
