package net.swordie.ms.connection.api;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.connection.crypto.SSL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApiAcceptor implements Runnable {
    private static final Logger log = LogManager.getLogger(ApiAcceptor.class);

    @Override
    public void run() {
        // Taken from http://netty.io/wiki/user-guide-for-4.x.html

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast("ssl", SSL.createHandler());
                    ch.pipeline().addLast("codec", new HttpServerCodec());
                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(1048576));
                    ch.pipeline().addLast("handler", new ApiHandler());
                }
            });

            b.childOption(ChannelOption.TCP_NODELAY, true);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(ServerConstants.API_PORT).sync();
            log.info(String.format("API listening on port %d", ServerConstants.API_PORT));

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
