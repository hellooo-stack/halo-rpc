package site.hellooo.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Author: Jeb.Wang
 * Time: 2023/6/28 22:05:11
 */
public class DefaultClient implements Client {
    private static final Logger logger = LoggerFactory.getLogger(DefaultClient.class);

    private Selector selector;
    private SocketChannel channel;
    private String host;
    private int port;
    private ClientConfig clientConfig;

    public DefaultClient(String host, int port, ClientConfig clientConfig) {
        this.host = host;
        this.port = port;
        this.clientConfig = clientConfig;
    }

    @Override
    public void send(byte[] message) throws IOException {
    }

    @Override
    public boolean connect() {
        if (channel != null && channel.isOpen() && channel.isConnected()) {
            logger.debug("connected");
            return true;
        }

        long begin = System.currentTimeMillis();
        for (int i = 0; i < clientConfig.getConnectRetryTimes(); i++) {
            try {

            } catch (Exception e) {
                logger.error("exp connecting remote.", e);
            }
        }

        return false;
    }

    private void doConnect() throws Exception {
        if (channel != null && channel.isOpen() && channel.isConnected()) {
            logger.debug("connected");
            return;
        }

        if (selector == null) {
            selector = Selector.open();
        }

        channel = SocketChannel.open();
        channel.configureBlocking(false);

        SocketAddress remoteAddress = new InetSocketAddress(host, port);
        boolean isConnected = channel.connect(remoteAddress);
        if (!isConnected && channel.isConnectionPending()) {
            isConnected = channel.finishConnect();
        }

    }

    @Override
    public boolean close() {
        return false;
    }
}
