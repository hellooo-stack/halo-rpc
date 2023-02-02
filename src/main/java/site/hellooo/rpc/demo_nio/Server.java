package site.hellooo.rpc.demo_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private String host;
    private int port;
    private boolean started;

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 29876;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Server() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }

    public void start() throws IOException {
        if (started) {
            throw new RuntimeException("server has been started.");
        }

        ServerSocketChannel serverChannel = ServerSocketChannel.open();

        InetSocketAddress serverAddress = new InetSocketAddress(host, port);
        serverChannel.bind(serverAddress);

        Selector selector = Selector.open();
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("server starting at " + host + ":" + port);
        this.started = true;

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (selectionKey.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
                    clientChannel.read(receiveBuffer);
                    String content = StandardCharsets.UTF_8.decode(receiveBuffer).toString();
                    System.out.println("data receive from client: " + content);
                } else if (selectionKey.isWritable()) {
                    SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
                    sendBuffer.put("hello client".getBytes(StandardCharsets.UTF_8));

                    clientChannel.write(sendBuffer);
                }
            }
        }
    }
}
