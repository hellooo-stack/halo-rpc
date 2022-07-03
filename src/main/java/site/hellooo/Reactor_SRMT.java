package site.hellooo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// single reactor multiple thread
public class Reactor_SRMT extends Reactor {
    private final int PORT;

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Reactor_SRMT(int port) throws IOException {
        PORT = port;
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
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    dispatcher(iterator.next());
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {

        }
    }

    private void dispatcher(SelectionKey selectionKey) {
        Runnable r = (Runnable) selectionKey.attachment();
        if (null != r) {
            r.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (null != socketChannel) {
                    new Handler(selector, socketChannel);
                }
            } catch (IOException e) {

            }
        }
    }
}

final class PoolHandler implements Runnable {

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;

    static ExecutorService pool = Executors.newFixedThreadPool(100);

    static final int MAX_IN = 1024;
    static final int MAX_OUT = 1024;
    ByteBuffer input = ByteBuffer.allocate(MAX_IN);
    ByteBuffer output = ByteBuffer.allocate(MAX_OUT);

    static final int READING = 0, SENDING = 1, PROCESSING = 3;
    int state = READING;

    public PoolHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    private boolean inputIsComplete() {
        return true;
    }

    private boolean outputIsComplete() {
        return true;
    }

    private void process() {

    }

    private void read() throws IOException {
        socketChannel.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            pool.execute(new Processor());
        }
        process();
        state = SENDING;
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    private void write() throws IOException {
        socketChannel.write(output);
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }

    private synchronized void processAndHandOff() {
        process();
        state = SENDING;
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                write();
            }
        } catch (IOException e) {

        }
    }

    class Processor implements Runnable {
        @Override
        public void run() {
            processAndHandOff();
        }
    }
}
