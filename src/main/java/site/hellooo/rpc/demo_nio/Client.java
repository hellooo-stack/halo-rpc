package site.hellooo.rpc.demo_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Client {

    private Selector selector;
    private SocketChannel clientChannel;
    private String host;
    private int port;

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 29876;

    public Client() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }

    public int send(String content) throws IOException {
        boolean connected = connect();
        if (!connected) {
            throw new RuntimeException("network error connecting");
        }

        clientChannel.register(selector, SelectionKey.OP_WRITE);
        int selected = selector.select();
        if (selected > 0) {
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isWritable()) {
                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                    writeBuffer.put(content.getBytes(StandardCharsets.UTF_8));
                    clientChannel.write(writeBuffer);
                }
            }
        }

        return 1;
    }

    private boolean connect() throws IOException {
        SocketAddress address = new InetSocketAddress(this.host, this.port);
        selector = Selector.open();
        clientChannel = SocketChannel.open();
        // todo 移除这行看下后面的connect会不会阻塞
        clientChannel.configureBlocking(false);
        boolean connected = clientChannel.connect(address);
        System.out.println("connected success? " + connected);
        if (!connected && clientChannel.isConnectionPending()) {
            connected = clientChannel.finishConnect();
        }
        if (!clientChannel.isConnected()) {
            clientChannel.register(selector, SelectionKey.OP_CONNECT);
            int selected = selector.select();
            if (selected > 0) {
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                if (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (clientChannel.finishConnect()) {
                        key.interestOps(key.interestOps() & (~SelectionKey.OP_CONNECT));
                    }
                }

            }

        }

        clientChannel.register(selector, SelectionKey.OP_READ);

        System.out.println("连接建立成功");
        return true;
    }
}



























