package site.hellooo.rpc.demo_reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// single reactor single thread
public class Reactor_SRST extends Reactor {
    private final int PORT;

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Reactor_SRST(int port) throws IOException {
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
//                todo 如果不clear会怎样？
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }
}

final class Handler implements Runnable {

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;

    static final int MAX_IN = 1024;
    static final int MAX_OUT = 1024;
//    todo 分配ByteBuffer的方式？
    ByteBuffer input = ByteBuffer.allocate(MAX_IN);
    ByteBuffer output = ByteBuffer.allocate(MAX_OUT);

    static final int READING = 0, SENDING = 1;
    int state = READING;

    public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
//        todo 是要先设置为非阻塞 然后才能register吗
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
//        todo 这里不写wakeup会怎样
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
        process();
        state = SENDING;
//        todo 读完了就注册写事件？那什么时候能写呢
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    private void write() throws IOException {
        socketChannel.write(output);
        if (outputIsComplete()) {
//            todo 这里关闭连接？
            selectionKey.cancel();
        }
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
            e.printStackTrace();
        }
    }
}
