package site.hellooo.rpc.demo_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    final Selector selector;
    final ServerSocketChannel serverSocketChannel;

    Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));

        serverSocketChannel.configureBlocking(false);
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    dispatch((SelectionKey) iterator.next());
                }
                selectionKeySet.clear();
            }
        } catch (IOException e) {

        }
    }

    void dispatch(SelectionKey selectionKey) {
        Runnable r = (Runnable) selectionKey.attachment();
        if (r != null) {
            r.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    new Handler(selector, socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

final class Handler implements Runnable {

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);
        // Optionally try first read now
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean inputIsComplete() {
        return false;
    }

    boolean outputIsComplete() {
        return false;
    }

    void process() {

    }

    void read() throws IOException {
        socketChannel.read(input);
        if (inputIsComplete()) {
            process();
            state = SENDING;
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException {
        socketChannel.write(output);
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }
}
