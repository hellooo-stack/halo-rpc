//package site.hellooo.rpc.client;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.SocketChannel;
//import java.util.Iterator;
//import java.util.Set;
//
//public class Client {
//    private int connectRetryTime = 10;
//    private int flow;
//    private String name;
//
//    public Client(int flow, String name) {
//        this.flow = flow;
//        this.name = name;
//    }
//
//    public int send() throws Exception {
//        long begin = System.currentTimeMillis();
//        try {
//            for (int i = 0; i < connectRetryTime; i++) {
//                int rt = connect();
//                if (rt == 0) {
//                    // connect success, break
//                    break;
//                }
//            }
//        } catch (Exception e) {
//
//        }
//
//        m_channel.register(m_selector, SelectionKey.OP_WRITE);
//        long now = System.currentTimeMillis();
//        for (; ; ) {
//            int sendTimeout = 3000;
//            int selected = m_selector.select(sendTimeout);
//            if (System.currentTimeMillis() - now > sendTimeout) {
//                return -1;
//            }
//
//            if (selected > 0) {
//                Set<SelectionKey> readyKeys = m_selector.selectedKeys();
//                Iterator<SelectionKey> iterator = readyKeys.iterator();
//                while (iterator.hasNext()) {
//                    SelectionKey key = iterator.next();
//                    iterator.remove();
//
//                    if (key.isWritable()) {
////                        long sendLen = send();
//                    }
//
//                    int newInterestOps = key.interestOps();
//                    newInterestOps &= ~SelectionKey.OP_WRITE;
//                    newInterestOps |= SelectionKey.OP_READ;
//                    key.interestOps(newInterestOps);
//                    return 0;
//                }
//            }
//        }
//
//        return 1;
//    }
//
//    public int recv() throws Exception {
//        long begin = System.currentTimeMillis();
//        try {
//            for (int i = 0; i < 3; i++) {
//                //conect()
//            }
//
//            Set<SelectionKey> readyKeys = m_selector.selectedKeys();
//            Iterator<SelectionKey> iterator = readyKeys.iterator();
//            while (iterator.hasNext()) {
//                SelectionKey key = iterator.next();
//                iterator.remove();
//                if (key.isReadable()) {
////                    long recvLen =
//
//                }
//            }
//        } catch (Exception e) {
//
//        }
//
//        return 0;
//    }
//
//    private long m_connectedTime;
//    private SocketChannel m_channel;
//    private Selector m_selector;
//
//    private int connect() {
//        long begin = System.currentTimeMillis();
//        if (m_connectedTime <= 0) {
//            m_connectedTime = begin;
//        }
//
//        try {
//            if (m_channel != null) {
//                if (m_channel.isConnected() && m_channel.isOpen()) {
//                    return 0;
//                }
//
//                try {
//                    m_channel.close();
//                } catch (IOException e) {
//                }
//            }
//
//            if (m_selector == null) {
//                m_selector = Selector.open();
//            }
//            m_channel = SocketChannel.open();
//            m_channel.configureBlocking(false);
//            m_channel.socket().setTcpNoDelay(true);
//
//            SocketAddress address = new InetSocketAddress("127.0.0.1", 8080);
//            boolean connected = m_channel.connect(address);
//            if (!connected && m_channel.isConnectionPending()) {
//                connected = m_channel.finishConnect();
//            }
//            if (!m_channel.isConnected()) {
//                int connectedTimeout = 3000;
//                m_channel.register(m_selector, SelectionKey.OP_CONNECT);
//                long now = System.currentTimeMillis();
//                for (; ; ) {
//                    int selected = m_selector.select(connectedTimeout);
//                    if (selected > 0) {
//                        Set<SelectionKey> readyKeys = m_selector.keys();
//                        Iterator<SelectionKey> iterator = readyKeys.iterator();
//                        if (iterator.hasNext()) {
//                            SelectionKey key = iterator.next();
//                            if (m_channel.finishConnect()) {
//                                key.interestOps(key.interestOps() & (~SelectionKey.OP_CONNECT));
//                                break;
//                            }
//                        }
//                    }
//
//                    if (System.currentTimeMillis() - now > connectedTimeout) {
//                        return -1;
//                    }
//                }
//            }
//
//            m_channel.register(m_selector, SelectionKey.OP_READ);
//            return 0;
//        } catch (Exception e) {
//
//        }
//
//        return 0;
//    }
//}
