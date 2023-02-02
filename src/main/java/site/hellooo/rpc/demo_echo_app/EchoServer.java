package site.hellooo.rpc.demo_echo_app;

import site.hellooo.rpc.demo_reactor.Reactor;
import site.hellooo.rpc.demo_reactor.Reactor_SRST;

import java.io.IOException;

public class EchoServer {

    private Reactor reactor;

    public void start() throws IOException {
        reactor = new Reactor_SRST(23111);
        reactor.run();
    }
}
