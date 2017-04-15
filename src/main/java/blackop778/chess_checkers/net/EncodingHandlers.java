package blackop778.chess_checkers.net;

import java.util.List;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import blackop778.chess_checkers.net.GameMessage.CheckersMessage;
import blackop778.chess_checkers.net.GameMessage.ChessMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

public abstract class EncodingHandlers {
    public static final Gson GSON = new Gson();

    public static Object decode(String msg) {
	String[] parts = msg.split(Pattern.quote(":"));
	if (parts[0].equals("CheckersMessage")) {
	    return GSON.fromJson(parts[1], CheckersMessage.class);
	} else if (parts[0].equals("ChessMessage")) {
	    return ChessMessage.instantiate(parts[1]);
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
	    return "ChessMessage:" + ((ChessMessage) obj).notation + "\n";
	} else if (obj instanceof CheckersMessage) {
	    return "CheckersMessage:" + GSON.toJson(obj) + "\n";
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
