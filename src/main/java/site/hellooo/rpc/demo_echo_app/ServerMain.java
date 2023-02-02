package site.hellooo.rpc.demo_echo_app;

import site.hellooo.rpc.demo_echo_app.EchoServer;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        EchoServer echoServer = new EchoServer();
        echoServer.start();
    }
}
