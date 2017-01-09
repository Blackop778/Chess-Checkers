package blackop778.chess_checkers.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server extends Client {

    public Server(boolean black, boolean gameIsCheckers) {
	super(black, gameIsCheckers);

    }

    public void startServer(int port) {
	EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
		    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
			    ChannelPipeline p = ch.pipeline();
			    p.addLast(new Server.ClientHandler());
			}
		    });

	    // Start the server.
	    ChannelFuture f = b.bind(port).sync();

	    // Wait until the server socket is closed.
	    f.channel().closeFuture().sync();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    // Shut down all event loops to terminate all threads.
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }
}
