package blackop778.chess_checkers.net;

import blackop778.chess_checkers.Chess_Checkers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server extends Client {

    public Server(boolean black, boolean gameIsCheckers, boolean localServer) {
	super(black, gameIsCheckers, localServer);
    }

    public void startServer(int port) {
	EventLoopGroup serverGroup;
	EventLoopGroup clientGroup = new NioEventLoopGroup();
	if (localServer) {
	    final LocalAddress local = new LocalAddress(String.valueOf(port));
	    serverGroup = new DefaultEventLoopGroup();
	    try {
		ServerBootstrap b = new ServerBootstrap();
		b.group(serverGroup, clientGroup).channel(LocalServerChannel.class)
			.handler(new ChannelInitializer<LocalServerChannel>() {
			    @Override
			    public void initChannel(LocalServerChannel ch) throws Exception {
				ch.pipeline().addLast(new LoggingHandler(LogLevel.ERROR));
			    }
			}).childHandler(new ChannelInitializer<LocalChannel>() {
			    @Override
			    public void initChannel(LocalChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new LoggingHandler(LogLevel.ERROR), new ClientHandler());
			    }
			});

		// Start the server.
		System.out.println("Server listening to port " + local.id());
		ChannelFuture f = b.bind(local).sync();
		Chess_Checkers.clientPartner = new Client(!black, gameIsCheckers, true);
		Chess_Checkers.clientPartner.start(clientGroup, local);

		// Wait until the server socket is closed.
		f.channel().closeFuture().sync();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } finally {
		// Shut down all event loops to terminate all threads.
		serverGroup.shutdownGracefully();
		clientGroup.shutdownGracefully();
	    }
	}
    }
}
