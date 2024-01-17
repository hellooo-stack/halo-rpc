package site.hellooo.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultServer implements IServer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServer.class);
    private ServerConfig serverConfig;

    private boolean started;

    public DefaultServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public void start() {
        if (started) {
            logger.warn("Server has started.");
            throw new RuntimeException("Server has started.");
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new MessageDecoder());
                            pipeline.addLast(new MessageEncoder());
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            final int port = serverConfig.getPort();
            final String host = serverConfig.getHost();
            ChannelFuture future = bootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
            started = true;
        } catch (InterruptedException e) {
            logger.error("Server start error!", e);
            throw new RuntimeException("Server start error!");
        } finally {
            try {
                bossGroup.shutdownGracefully().sync();
                workerGroup.shutdownGracefully();
            } catch (InterruptedException e) {
                logger.error("Shutdown channel error!", e);
            }
        }
    }

    @Override
    public void stop() {

    }
}
