package site.hellooo;

import java.io.IOException;

public class EchoServer {

    private Reactor reactor;

    public void start() throws IOException {
        reactor = new Reactor_SRST(23111);
        reactor.run();
    }
}
