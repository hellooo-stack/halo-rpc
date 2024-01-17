package site.hellooo.rpc.server;

public interface IRegistry {
    void register(ProviderConfig providerConfig);

    void unRegister(ProviderConfig providerConfig);
}
