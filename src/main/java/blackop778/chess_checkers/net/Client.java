package blackop778.chess_checkers.net;

import blackop778.chess_checkers.pieces.Piece;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Client extends ChannelInboundHandlerAdapter {

    private Piece[][] board;
    public final boolean black;
    public final boolean gameIsCheckers;

    public Client(boolean black, boolean gameIsCheckers) {
	this.black = black;
	this.gameIsCheckers = gameIsCheckers;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	if (msg instanceof EventMessage) {
	    EventMessage event = (EventMessage) msg;
	}
    }

    public Piece[][] getBoard() {
	return board;
    }
}