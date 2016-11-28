package blackop778.chess_checkers.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Client extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	if (msg instanceof EventMessage) {
	    EventMessage event = (EventMessage) msg;
	}
    }
}