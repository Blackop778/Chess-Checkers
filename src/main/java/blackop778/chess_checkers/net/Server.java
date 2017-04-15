package blackop778.chess_checkers.net;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.net.EncodingHandlers.EncodableInboundHandler;
import blackop778.chess_checkers.net.EncodingHandlers.EncodableOutboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

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
		b.group(serverGroup, clientGroup).channel(LocalServerChannel.class).childHandler(new ServerHandler());

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
			.childHandler(new ChannelInitializer<SocketChannel>() {
			    @Override
			    public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				// Decoders
				p.addLast("frameDecoder", new LineBasedFrameDecoder(80));
				p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
				p.addLast("messageDecoder", new EncodableInboundHandler());
				// Encoders
				p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
				p.addLast("messageEncoder", new EncodableOutboundHandler());
				p.addLast("C_CProcessor", new ServerHandler());
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
