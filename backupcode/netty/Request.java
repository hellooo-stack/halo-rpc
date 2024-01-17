package site.hellooo.rpc.server;

public class Request {
    private String interfaceId;
    private String methodName;
    private Class<?>[] paramType;
    private Object[] parameters;
}
