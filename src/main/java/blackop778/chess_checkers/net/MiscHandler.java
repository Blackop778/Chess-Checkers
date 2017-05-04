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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvalidMessageException {
	if (msg instanceof HandshakeMessage) {
	    Chess_Checkers.debugLog("Recieved HandshakeMessage");
	    HandshakeMessage hm = (HandshakeMessage) msg;
	    if (hm.black == Chess_Checkers.client.getBlack()) {
		if (Chess_Checkers.client instanceof Server) {
		    new ColorConflictCorrection(hm);
		} else {
		    /**
		     * dialog = InfoDialog.showMessage( "Both players tried to
		     * choose the same color, Server host is choosing how to
		     * decide.", "Patience");
		     */
		    Chess_Checkers.debugLog("Not displaying color conflict notification");
		}
	    }
	} else if (msg instanceof ColorConflictMessage) {
	    Chess_Checkers.debugLog("Recieved ColorConflictMessage");
	    ColorConflictMessage ccm = (ColorConflictMessage) msg;
	    Chess_Checkers.client.resolveColorConflict(ccm.ccc);
	} else if (msg instanceof ColorAgreementMessage) {
	    Chess_Checkers.debugLog("Recieved ColorAgreementMessage");
	    ColorAgreementMessage cam = (ColorAgreementMessage) msg;
	    if (dialog != null && dialog.isDisplaying()) {
		dialog.close();
	    }
	    if (cam.senderBlack == Chess_Checkers.client.getBlack()) {
		throw new InvalidMessageException("Colors supposed to be corrected but are conflicting");
	    } else {
		// Start GUI
		Chess_Checkers.debugLog("Now playing as: " + (Chess_Checkers.client.getBlack() ? "Black" : "White"));
		Chess_Checkers.debugLog("Starting game display");
		Chess_Checkers.startGUI(" " + ((Chess_Checkers.client instanceof Server) ? "server" : "client"));
	    }
	} else {
	    Chess_Checkers.debugLog(
		    "WARNING: Class " + msg.getClass().getSimpleName() + " was not handled by either handler");
	}
    }
}
