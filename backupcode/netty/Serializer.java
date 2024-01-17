package site.hellooo.rpc.server;

public interface Serializer {
    <T> byte[] serialize(T t);

    <T> T deserialize(byte[] bytes);
}
