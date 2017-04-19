package blackop778.chess_checkers.net;

import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import blackop778.chess_checkers.net.ColorConflictMessage.ColorAgreementMessage;
import blackop778.chess_checkers.net.GameMessage.CheckersMessage;
import blackop778.chess_checkers.net.GameMessage.ChessMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class EncodingHandlers {
    public static final Gson GSON = new Gson();
    private static final String MESSAGE_SEPARATOR = ":$#:";

    public static Object decode(String msg) {
	String[] parts = msg.split(Pattern.quote(MESSAGE_SEPARATOR));
	if (parts[0].equals("CheckersMessage")) {
	    return GSON.fromJson(parts[1], CheckersMessage.class);
	} else if (parts[0].equals("ChessMessage")) {
	    return ChessMessage.instantiate(parts[1]);
	} else if (parts[0].equals("HandshakeMessage")) {
	    return GSON.fromJson(parts[1], HandshakeMessage.class);
	} else if (parts[0].equals("ColorConflictMessage")) {
	    return GSON.fromJson(parts[1], ColorConflictMessage.class);
	} else if (parts[0].equals("ColorAgreementMessage")) {
	    return GSON.fromJson(parts[1], ColorAgreementMessage.class);
	} else {
	    throw new ClassCastException(parts[0] + " is not an expected message type");
	}
    }

    public static class EncodableInboundHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
	    ctx.fireChannelRead(decode(msg));
	}

    }

    public static String encode(Object obj) {
	if (obj instanceof ChessMessage) {
	    return "ChessMessage" + MESSAGE_SEPARATOR + ((ChessMessage) obj).notation + "\n";
	} else if (obj instanceof CheckersMessage) {
	    return "CheckersMessage" + MESSAGE_SEPARATOR + GSON.toJson(obj) + "\n";
	} else if (obj instanceof HandshakeMessage) {
	    return "HandshakeMessage" + MESSAGE_SEPARATOR + GSON.toJson(obj) + "\n";
	} else if (obj instanceof ColorConflictMessage) {
	    return "ColorConflictMessage" + MESSAGE_SEPARATOR + GSON.toJson(obj);
	} else if (obj instanceof ColorAgreementMessage) {
	    return "ColorAgreementMessage" + MESSAGE_SEPARATOR + GSON.toJson(obj);
	} else {
	    throw new ClassCastException(obj.getClass().getName() + " is not an expected message type");
	}
    }

    public static class EncodableOutboundHandler extends MessageToMessageEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
	    out.add(EncodingHandlers.encode(msg));
	}

    }
}
