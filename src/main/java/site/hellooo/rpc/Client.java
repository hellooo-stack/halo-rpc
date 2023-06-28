package site.hellooo.rpc;

import java.io.IOException;

public interface Client {
    void send(byte[] message) throws IOException;

    boolean connect();

    boolean close();
}

