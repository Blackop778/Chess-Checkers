package blackop778.chess_checkers.net;

import blackop778.chess_checkers.Chess_Checkers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server extends Client {

    public Server(boolean black, boolean gameIsCheckers, boolean localServer) {
	super(black, gameIsCheckers, localServer);
    }

    public void startServer(int port) {
	EventLoopGroup serverGroup;
	EventLoopGroup clientGroup = new NioEventLoopGroup();
	ServerBootstrap b;
	if (localServer) {
	    final LocalAddress local = new LocalAddress(String.valueOf(port));
	    serverGroup = new DefaultEventLoopGroup();
	    try {
		b = new ServerBootstrap();
		b.group(serverGroup, clientGroup).channel(LocalServerChannel.class)
			.handler(new ChannelInitializer<LocalServerChannel>() {
			    @Override
			    public void initChannel(LocalServerChannel ch) throws Exception {
				ch.pipeline().addLast(new LoggingHandler(LogLevel.WARN));
			    }
			}).childHandler(new ChannelInitializer<LocalChannel>() {
			    @Override
			    public void initChannel(LocalChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new LoggingHandler(LogLevel.WARN), new ServerHandler());
			    }
			});

		// Start the server.
		System.out.println("Server listening to port " + local.id());
		ChannelFuture f = b.bind(local).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE).sync();
		Chess_Checkers.clientPartner = new Client(!black, gameIsCheckers, true);
		Chess_Checkers.clientPartner.startLocal(clientGroup, local);

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
	} else {
	    serverGroup = new NioEventLoopGroup();
	    try {
		b = new ServerBootstrap();
		b.group(serverGroup, clientGroup).channel(NioServerSocketChannel.class)
			.handler(new LoggingHandler(LogLevel.WARN))
			.childHandler(new ChannelInitializer<SocketChannel>() {
			    @Override
			    public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new LoggingHandler(LogLevel.WARN), new ServerHandler());
			    }
			});

		// Start the server.
		System.out.println("Server listening to port " + port);
		ChannelFuture f = b.bind(port).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE).sync();

		// Wait until the server socket is closed.
		f.channel().closeFuture().sync();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } finally {
		// Shut down all event loops to terminate all threads.
		serverGroup.shutdownGracefully();
		clientGroup.shutdownGracefully();
	    }
	}
    }

    public class ServerHandler extends ClientHandler {
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	    super.channelActive(ctx);
	    System.out.println("Starting game display");
	    Chess_Checkers.startGUI(" server");
	}
    }
}
