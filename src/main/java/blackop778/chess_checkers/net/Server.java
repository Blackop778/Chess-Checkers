package blackop778.chess_checkers.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
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

    private Client partner;
    private ChannelHandlerContext context;

    public Server(boolean black, boolean gameIsCheckers) {
	super(black, gameIsCheckers);

    }

    public void startServer(int port) {
	final LocalAddress local = new LocalAddress("1778");
	EventLoopGroup serverGroup = new DefaultEventLoopGroup();
	EventLoopGroup clientGroup = new NioEventLoopGroup();
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(serverGroup, clientGroup).channel(LocalServerChannel.class)
		    .handler(new ChannelInitializer<LocalServerChannel>() {
			@Override
			public void initChannel(LocalServerChannel ch) throws Exception {
			    ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
			}
		    }).childHandler(new ChannelInitializer<LocalChannel>() {
			@Override
			public void initChannel(LocalChannel ch) throws Exception {
			    ChannelPipeline p = ch.pipeline();
			    p.addLast(new LoggingHandler(LogLevel.INFO), new ServerHandler());
			}
		    });

	    // Start the server.
	    System.out.println("Server listening to port " + port);
	    ChannelFuture f = b.bind(local).sync();
	    partner = new Client(false, gameIsCheckers);
	    partner.start(clientGroup, local);

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

    public class ServerHandler extends ClientHandler {
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	    context = ctx;
	}
    }
}
