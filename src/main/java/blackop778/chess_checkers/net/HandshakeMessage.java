package blackop778.chess_checkers.net;

import javax.swing.JOptionPane;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.graphics.ColorConflictCorrection;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HandshakeMessage {
    public final boolean black;
    public final long seed;

    // More to be added
    public HandshakeMessage() {
	black = Chess_Checkers.client.getBlack();
	seed = System.currentTimeMillis();
    }

    public static class HandshakeHandler extends SimpleChannelInboundHandler<HandshakeMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HandshakeMessage msg) throws Exception {
	    if (Chess_Checkers.client instanceof Server) {
		new ColorConflictCorrection(msg);
	    } else {
		JOptionPane.showMessageDialog(null,
			"Both players tried to choose the same color, Server host is choosing how to decide.",
			"Patience", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }
}
