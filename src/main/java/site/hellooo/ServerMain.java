package site.hellooo;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        EchoServer echoServer = new EchoServer();
        echoServer.start();
    }
}
