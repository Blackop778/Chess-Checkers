package blackop778.chess_checkers.net;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.graphics.ColorConflictCorrection;
import blackop778.chess_checkers.graphics.InfoDialog;
import blackop778.chess_checkers.net.ColorConflictMessage.ColorAgreementMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MiscHandler extends ChannelInboundHandlerAdapter {
    public static InfoDialog dialog;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
	try {
	    if (msg instanceof HandshakeMessage) {
		HandshakeMessage hm = (HandshakeMessage) msg;
		if (hm.black == Chess_Checkers.client.getBlack()) {
		    if (Chess_Checkers.client instanceof Server) {
			new ColorConflictCorrection(hm);
		    } else {
			dialog = InfoDialog.showMessage(
				"Both players tried to choose the same color, Server host is choosing how to decide.",
				"Patience");
		    }
		}
	    } else if (msg instanceof ColorConflictMessage) {
		ColorConflictMessage ccm = (ColorConflictMessage) msg;
		Chess_Checkers.client.resolveColorConflict(ccm.ccc);
		ctx.write(new ColorAgreementMessage(Chess_Checkers.client.getBlack()));
	    } else if (msg instanceof ColorAgreementMessage) {
		ColorAgreementMessage cam = (ColorAgreementMessage) msg;
		if (cam.senderBlack == Chess_Checkers.client.getBlack()) {
		    throw new InvalidMessageException("Colors supposed to be corrected but are conflicting");
		}
	    }
	} catch (InvalidMessageException e) {

	}
    }
}
