package site.hellooo.rpc.demo_nio;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.send("my first cs app, and this message is from client");
    }
}
