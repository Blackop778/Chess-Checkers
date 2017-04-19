package blackop778.chess_checkers.net;

import java.awt.Point;
import java.net.ConnectException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.Utilities;
import blackop778.chess_checkers.checkers.Jump;
import blackop778.chess_checkers.checkers.JumpTree;
import blackop778.chess_checkers.chess.PawnPromotion;
import blackop778.chess_checkers.chess.PawnPromotion.Promotion;
import blackop778.chess_checkers.chess.SnapshotStorage;
import blackop778.chess_checkers.graphics.ColorConflictCorrection;
import blackop778.chess_checkers.net.ColorConflictMessage.ColorAgreementMessage;
import blackop778.chess_checkers.net.EncodingHandlers.EncodableInboundHandler;
import blackop778.chess_checkers.net.EncodingHandlers.EncodableOutboundHandler;
import blackop778.chess_checkers.net.GameMessage.CheckersMessage;
import blackop778.chess_checkers.net.GameMessage.ChessMessage;
import blackop778.chess_checkers.net.GameMessage.Direction;
import blackop778.chess_checkers.net.GameMessage.EvaluatedChessMessage;
import blackop778.chess_checkers.pieces.Bishop;
import blackop778.chess_checkers.pieces.Checker;
import blackop778.chess_checkers.pieces.CheckersPiece;
import blackop778.chess_checkers.pieces.ChessPiece;
import blackop778.chess_checkers.pieces.Empty;
import blackop778.chess_checkers.pieces.King;
import blackop778.chess_checkers.pieces.Knight;
import blackop778.chess_checkers.pieces.Pawn;
import blackop778.chess_checkers.pieces.Piece;
import blackop778.chess_checkers.pieces.Queen;
import blackop778.chess_checkers.pieces.Rook;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class Client {

    private Piece[][] board;
    private boolean black;
    public final boolean gameIsCheckers;
    private boolean turn;
    private ChannelHandlerContext context;
    protected final boolean localServer;

    public Client(boolean black, boolean gameIsCheckers, boolean localServer) {
	this.black = black;
	this.gameIsCheckers = gameIsCheckers;
	this.localServer = localServer;

	board = new Piece[8][8];
	for (int i = 0; i < board.length; i++) {
	    for (int n = 0; n < board[0].length; n++) {
		board[i][n] = new Empty();
	    }
	}

	if (gameIsCheckers) {
	    ChessPiece.threefoldRepetition = false;
	    SnapshotStorage.initialize(false);
	    gameIsCheckers = true;
	    turn = black;
	    for (int i = 0; i < board.length; i++) {
		for (int n = 0; n < board[0].length; n++) {
		    if ((i + n) % 2 == 1) {
			if (n < 3) {
			    board[i][n] = new Checker(true, n + (i * 8));
			} else if (n > 4) {
			    board[i][n] = new Checker(false, n + (i * 8));
			}
		    }
		}
	    }
	} else {
	    ChessPiece.threefoldRepetition = true;
	    SnapshotStorage.initialize(true);
	    ChessPiece.doubleMovePawn = null;
	    ChessPiece.pawnCaptureCount = 0;
	    gameIsCheckers = false;
	    turn = !black;
	    for (int i = 0; i < board.length; i++) {
		for (int n = 0; n < board[0].length; n++) {
		    switch (n) {
		    case 0:
			if (i == 0 || i == 7) {
			    board[i][n] = new Rook(true);
			} else if (i == 1 || i == 6) {
			    board[i][n] = new Knight(true);
			} else if (i == 2 || i == 5) {
			    board[i][n] = new Bishop(true);
			} else if (i == 3) {
			    board[i][n] = new Queen(true);
			} else if (i == 4) {
			    board[i][n] = new King(true);
			}
			break;
		    case 1:
			board[i][n] = new Pawn(true);
			break;

		    case 6:
			board[i][n] = new Pawn(false);
			break;
		    case 7:
			if (i == 0 || i == 7) {
			    board[i][n] = new Rook(false);
			} else if (i == 1 || i == 6) {
			    board[i][n] = new Knight(false);
			} else if (i == 2 || i == 5) {
			    board[i][n] = new Bishop(false);
			} else if (i == 3) {
			    board[i][n] = new Queen(false);
			} else if (i == 4) {
			    board[i][n] = new King(false);
			}
			break;
		    }
		}
	    }
	}
    }

    public class ClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvalidMessageException {
	    if (msg instanceof GameMessage) {
		turn = true;
		if (msg instanceof ChessMessage) {
		    EvaluatedChessMessage m = new EvaluatedChessMessage((ChessMessage) msg);
		    if (m.offerDraw && (!Chess_Checkers.getNotation().endsWith("(=)")
			    || !Chess_Checkers.getNotation().endsWith("(=)\n"))) {
			int response = JOptionPane.showConfirmDialog(null,
				"You opponent proposes a draw. Do you accept?", "Draw proposal",
				JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
			    passTurn(ChessMessage.instantiate(".5-.5"));
			} else {
			    passTurn(ChessMessage.instantiate("(=)"));
			}
		    } else if (m.offerSurrender && (!Chess_Checkers.getNotation().endsWith("(=+)")
			    || !Chess_Checkers.getNotation().endsWith("(=+)\n"))) {
			int response = JOptionPane.showConfirmDialog(null,
				"You opponent offers their surrender. Do you accept?", "Surrender offer",
				JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION) {
			    if (black) {
				passTurn(ChessMessage.instantiate("0-1"));
			    } else {
				passTurn(ChessMessage.instantiate("1-0"));
			    }
			} else {

			}
		    } else if (m.draw) {
			JOptionPane.showMessageDialog(null, "Your match ends in an honorable draw.");
		    } else if (m.blackWin) {

		    } else if (m.whiteWin) {

		    } else if (m.castleDirection == Direction.NONE) {
			if (board[m.fromX][m.fromY] instanceof Pawn) {
			    // Check En passant capturing
			    if (Math.abs(m.toX - m.fromX) == 1 && board[m.toX][m.toY] instanceof Empty) {
				board[m.toX][m.fromY] = new Empty();
			    }
			}
			board[m.toX][m.toY] = board[m.fromX][m.fromY];
			board[m.fromX][m.fromY] = new Empty();
			if (board[m.toX][m.toY] instanceof Pawn) {
			    ChessPiece.pawnCaptureCount = 0;
			    if (Math.abs(m.toY - m.fromY) == 2) {
				if (m.fromY == 1 || m.fromY == 6) {
				    ChessPiece.doubleMovePawn = (Pawn) board[m.toX][m.toY];
				}
			    } else {
				switch (m.pawnPromotion) {
				case QUEEN:
				    board[m.toX][m.toY] = new Queen(!black);
				    break;
				case KNIGHT:
				    board[m.toX][m.toY] = new Knight(!black);
				    break;
				case ROOK:
				    board[m.toX][m.toY] = new Rook(!black);
				    break;
				case BISHOP:
				    board[m.toX][m.toY] = new Bishop(!black);
				    break;
				case NONE:
				    break;
				}
			    }
			} else if (board[m.toX][m.toY] instanceof King) {
			    King pieceK = (King) board[m.toX][m.toY];
			    pieceK.moved = true;
			} else if (board[m.toX][m.toY] instanceof Rook) {
			    Rook pieceR = (Rook) board[m.toX][m.toY];
			    pieceR.moved = true;
			}
			// Castling
		    } else {
			int y;
			if (black) {
			    y = 7;
			} else {
			    y = 0;
			}
			boolean right = m.castleDirection == Direction.RIGHT;
			King pieceK = (King) board[m.fromX][y];
			Rook pieceR = (Rook) board[right ? 7 : 0][y];
			if (!pieceR.moved && !pieceK.moved) {
			    board[right ? 5 : 3][y] = board[right ? 7 : 0][y];
			    board[right ? 7 : 0][y] = new Empty();
			    board[m.toX][y] = board[m.fromX][y];
			    board[m.fromX][y] = new Empty();
			}
		    }
		    System.out.println(((ChessMessage) msg).notation);
		    Chess_Checkers.updateNotation(((ChessMessage) msg).notation);
		} else if (msg instanceof CheckersMessage) {
		    CheckersMessage event = (CheckersMessage) msg;
		    Point p = GameMessage.chessNotationToPoint(event.coordinate1);
		    int x = p.x;
		    int y = p.y;
		    for (Jump j : event.tree.getMidJumps()) {
			if (j.getMidPoint() != null) {
			    board[j.getMidPoint().x][j.getMidPoint().y] = new Empty();
			}
		    }
		    if (event.tree.getEndJump().getMidPoint() != null) {
			board[event.tree.getEndJump().getMidPoint().x][event.tree.getEndJump()
				.getMidPoint().y] = new Empty();
		    }
		    if ((event.tree.getEndJump().getEndPoint().y == 0 && !board[x][y].black)
			    || (event.tree.getEndJump().getEndPoint().y == 7 && board[x][y].black)) {
			Checker c = (Checker) board[x][y];
			c.kinged = true;
		    }
		    board[event.tree.getEndJump().getEndPoint().x][event.tree.getEndJump()
			    .getEndPoint().y] = board[x][y];
		    board[x][y] = new Empty();
		}
		if (Chess_Checkers.panel != null) {
		    Chess_Checkers.panel.repaint();
		}
	    } else {
		ctx.fireChannelRead(msg);
	    }
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	    context = ctx;
	    Chess_Checkers.debugLog("Sending HandshakeMessage");
	    ctx.writeAndFlush(new HandshakeMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	    System.out.println("Exception Caught yo");
	    if (cause instanceof ConnectException) {
		Chess_Checkers.setup.redisplay();
		Chess_Checkers.setup();
	    } else {
		ctx.fireExceptionCaught(cause);
	    }
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
	    Chess_Checkers.debugLog("Channel is now Inactive");
	}
    }

    public void startLocal(EventLoopGroup group, SocketAddress local) {
	if (localServer) {
	    try {
		Bootstrap b = new Bootstrap();
		b.group(group).channel(LocalChannel.class).handler(new ClientHandler());

		// Start the client.
		ChannelFuture future = b.connect(local).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
			.sync();

		// Start GUI
		Chess_Checkers.debugLog("Starting game display");
		Chess_Checkers.startGUI(" server");

		// Wait until the connection is closed.
		future.channel().closeFuture().sync();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } finally {
		// Shut down the event loop to terminate all threads.
		group.shutdownGracefully();
	    }
	}
    }

    public void start(String ip, int port) {
	if (!localServer) {
	    EventLoopGroup event = new NioEventLoopGroup();
	    try {
		Bootstrap b = new Bootstrap();
		b.group(event).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
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
			// Handlers
			p.addLast("c_cClientProcessor", new ClientHandler());
			p.addLast("miscHandler", new MiscHandler());
		    }
		});

		// Start the client.
		ChannelFuture future = b.connect(ip, port).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
			.sync();

		if (!future.isSuccess()) {
		    System.out.println("Future not successful. Printing error: ");
		    future.cause().printStackTrace();
		}

		// Wait until the connection is closed.
		future.channel().closeFuture().sync();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } finally {
		// Shut down the event loop to terminate all threads.
		event.shutdownGracefully();
	    }
	}
    }

    private void passTurn(GameMessage m) {
	turn = false;
	context.writeAndFlush(m);
	if (localServer) {
	    Client t = Chess_Checkers.client;
	    Chess_Checkers.client = Chess_Checkers.clientPartner;
	    Chess_Checkers.clientPartner = t;
	}
    }

    public void select(Point point, Piece selector) {
	select(point.x, point.y, selector);
    }

    public void select(int x, int y, Piece selector) {
	board[x][y].possible = true;
	board[x][y].selector = selector;
    }

    public void moveChecker(int x, int y, Checker checker) {
	// Reset possible spots for the next piece to move
	Chess_Checkers.client.unselectAll();
	for (JumpTree tree : checker.lastValidLocations) {
	    // Find which jumptree we're actually following
	    if (x == tree.getEndJump().getEndPoint().x && y == tree.getEndJump().getEndPoint().y) {
		ArrayList<Jump> midJumpsAL = tree.getMidJumps();
		Jump[] midJumps = new Jump[0];
		// Convert the mid jumps to an array for easier handling
		midJumps = midJumpsAL.toArray(midJumps);
		for (Jump jump : midJumps) {
		    if (jump != null) {
			// Make the piece(s) we jumped over's place(s) empty
			board[jump.getMidPoint().x][jump.getMidPoint().y] = new Empty();
		    }
		}
		// if we are jumping the piece
		if (tree.getEndJump().getMidPoint() != null) {
		    // Clear the last piece to be jumped over in the train
		    board[tree.getEndJump().getMidPoint().x][tree.getEndJump().getMidPoint().y] = new Empty();
		}
		// Find our current square in the board and make it empty
		int i;
		int n = 0;
		findSelfLoop: for (i = 0; i < 8; i++) {
		    for (n = 0; n < 8; n++) {
			if (board[i][n].equals(checker)) {
			    board[i][n] = new Empty();
			    break findSelfLoop;
			}
		    }
		}
		// Check if we should be kinged
		if (black && y == 7) {
		    checker.kinged = true;
		} else if (!black && y == 0) {
		    checker.kinged = true;
		}
		// Actually put ourselves on the board in the new place
		board[x][y] = checker;
		// Check if the other team has any possible moves
		if (Utilities.isArrayEmpty(CheckersPiece.checkJumps(!black, true))) {
		    Chess_Checkers.gameOver = true;
		    String winner = black ? "black" : "red";
		    JOptionPane.showMessageDialog(null,
			    "Congratulations, " + winner
				    + " wins. Exit this message and click on the board to restart.",
			    "A Champion has been decided!", JOptionPane.INFORMATION_MESSAGE);
		}
		passTurn(CheckersMessage.instantiate(GameMessage.pointToChessNotation(i, n), tree, false));
		// End the search for the jumptree we took and end the
		// method
		break;
	    }
	}
    }

    public void moveChess(int x, int y, ChessPiece piece) {
	ChessMessage cm = null;
	String message;
	Chess_Checkers.client.unselectAll();
	int i;
	int n = 0;
	findSelfLoop: for (i = 0; i < 8; i++) {
	    for (n = 0; n < 8; n++) {
		if (board[i][n].equals(piece)) {
		    board[i][n] = new Empty();
		    break findSelfLoop;
		}
	    }
	}

	boolean capture = false;
	if (piece instanceof King) {
	    message = "K";
	    King pieceK = (King) piece;
	    if (!pieceK.moved) {
		pieceK.moved = true;
		if (x == 6) {
		    cm = ChessMessage.instantiate("0-0");
		    board[5][y] = board[7][y];
		    board[7][y] = new Empty();
		} else if (x == 2) {
		    cm = ChessMessage.instantiate("0-0-0");
		    board[3][y] = board[0][y];
		    board[0][y] = new Empty();
		}
	    }
	} else if (piece instanceof Rook) {
	    message = "R";
	    Rook pieceR = (Rook) piece;
	    if (!pieceR.moved) {
		pieceR.moved = true;
	    }
	} else if (piece instanceof Pawn) {
	    message = "";
	    Pawn pieceP = (Pawn) piece;
	    ChessPiece.pawnCaptureCount = 0;
	    int yOffset = black ? -1 : 1;
	    // Check En passant capturing
	    if (board[x][y + yOffset].equals(ChessPiece.doubleMovePawn)) {
		board[x][y + yOffset] = new Empty();
		capture = true;
	    }
	    ChessPiece.doubleMovePawn = pieceP;
	} else if (piece instanceof Bishop) {
	    message = "B";
	} else if (piece instanceof Knight) {
	    message = "N";
	} else if (piece instanceof Queen) {
	    message = "Q";
	} else
	    throw new RuntimeException("Error: Unknown and unhandled piece type");

	if (cm == null) {
	    message = message + GameMessage.pointToChessNotation(i, n);
	    if (!(board[x][y] instanceof Empty) || capture) {
		message = message + "x";
	    } else {
		message = message + "-";
	    }
	    message = message + GameMessage.pointToChessNotation(x, y);
	}

	if (piece instanceof Pawn) {
	    if (y == 0 || y == 7) {
		PawnPromotion promoter = new PawnPromotion();
		Promotion promotion = promoter.result;
		switch (promotion) {
		case QUEEN:
		    piece = new Queen(black);
		    message = message + "Q";
		    break;
		case KNIGHT:
		    piece = new Knight(black);
		    message = message + "N";
		    break;
		case ROOK:
		    piece = new Rook(black);
		    message = message + "R";
		    break;
		case BISHOP:
		    piece = new Bishop(black);
		    message = message + "B";
		    break;
		case NONE:
		    System.exit(1);
		}
	    }
	} else {
	    if (board[x][y] instanceof Empty) {
		ChessPiece.pawnCaptureCount++;
	    } else {
		ChessPiece.pawnCaptureCount = 0;
	    }
	    ChessPiece.doubleMovePawn = null;
	}
	board[x][y] = piece;
	if (ChessPiece.isKingInCheck(!black)) {
	    if (ChessPiece.canMove(!black)) {
		message = message + "+";
	    } else {
		message = message + "#";
	    }
	}
	ChessPiece.endGameCheck();

	passTurn((cm == null) ? ChessMessage.instantiate(message) : cm);
    }

    public static class UnexpectedActionException extends Exception {
	public UnexpectedActionException(String string) {
	    super(string);
	}

	private static final long serialVersionUID = -5532979511854500096L;
    }

    public void unselectAll() {
	for (Piece[] row : board) {
	    for (Piece piece : row) {
		piece.possible = false;
		piece.selected = false;
	    }
	}
    }

    public void resolveColorConflict(ColorConflictCorrection ccc) {
	if (ccc.isRandomSelected()) {
	    Random random = new Random((ccc.getMSG().seed + Chess_Checkers.ourSeed) / 3);
	    boolean answer = random.nextBoolean();
	    if (!answer) {
		black = !black;
	    }
	    if (this instanceof Server) {
		Chess_Checkers.debugLog("Sending ColorConflictMessage");
		context.writeAndFlush(ccc);
	    }
	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    Chess_Checkers.debugLog("Sending ColorAgreementMessage");
	    context.writeAndFlush(new ColorAgreementMessage(black));
	} else {

	}
	if (!(this instanceof Server)) {
	    black = !black;
	}
    }

    public void offer(String message) {
	if (message.equals("(=)") || message.equals("(=+)")) {
	    passTurn(ChessMessage.instantiate(message));
	}
    }

    public Piece[][] getBoard() {
	return board;
    }

    public boolean getTurn() {
	return turn;
    }

    public boolean getBlack() {
	return new Boolean(black);
    }
}