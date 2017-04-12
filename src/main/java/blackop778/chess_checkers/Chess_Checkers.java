package blackop778.chess_checkers;

import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import blackop778.chess_checkers.graphics.Chess_CheckersPanel;
import blackop778.chess_checkers.graphics.Setup;
import blackop778.chess_checkers.net.Client;
import blackop778.chess_checkers.net.Server;
import blackop778.chess_checkers.pieces.Bishop;
import blackop778.chess_checkers.pieces.King;
import blackop778.chess_checkers.pieces.Knight;
import blackop778.chess_checkers.pieces.Pawn;
import blackop778.chess_checkers.pieces.Queen;
import blackop778.chess_checkers.pieces.Rook;

public abstract class Chess_Checkers {
    public static Client client;
    public static Client clientPartner;
    public static boolean offerSurrender;
    public static boolean gameOver;
    public static Chess_CheckersPanel panel;
    public static String notation;
    public static Setup setup;
    public static final boolean DISABLE_AI = true;
    public static final boolean DISABLE_INTERNET = true;
    private static boolean imagesLoaded;

    public static void main(String[] args) {
	imagesLoaded = false;
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		final String baseFilePath = "assets" + File.separator + "images" + File.separator;
		final String blackPrefix = "Black";
		final String whitePrefix = "White";
		final ClassLoader cl = ClassLoader.getSystemClassLoader();
		try {
		    Bishop.blackImage = ImageIO.read(cl.getResource(baseFilePath + blackPrefix + "Bishop.png"));
		    Bishop.whiteImage = ImageIO.read(cl.getResource(baseFilePath + whitePrefix + "Bishop.png"));
		    King.blackImage = ImageIO.read(cl.getResource(baseFilePath + blackPrefix + "King.png"));
		    King.whiteImage = ImageIO.read(cl.getResource(baseFilePath + whitePrefix + "King.png"));
		    Knight.blackImage = ImageIO.read(cl.getResource(baseFilePath + blackPrefix + "Knight.png"));
		    Knight.whiteImage = ImageIO.read(cl.getResource(baseFilePath + whitePrefix + "Knight.png"));
		    Pawn.blackImage = ImageIO.read(cl.getResource(baseFilePath + blackPrefix + "Pawn.png"));
		    Pawn.whiteImage = ImageIO.read(cl.getResource(baseFilePath + whitePrefix + "Pawn.png"));
		    Queen.blackImage = ImageIO.read(cl.getResource(baseFilePath + blackPrefix + "Queen.png"));
		    Queen.whiteImage = ImageIO.read(cl.getResource(baseFilePath + whitePrefix + "Queen.png"));
		    Rook.blackImage = ImageIO.read(cl.getResource(baseFilePath + blackPrefix + "Rook.png"));
		    Rook.whiteImage = ImageIO.read(cl.getResource(baseFilePath + whitePrefix + "Rook.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setImagesLoaded(true);
	    }
	}, "ImageLoader").start();
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
		| UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	}

	setup = new Setup();
	setup();
    }

    public static void startGUI(String name) {
	JFrame frame = new JFrame((GraphicsConfiguration) null);
	panel = new Chess_CheckersPanel();
	frame.add(Chess_Checkers.panel);
	frame.setTitle("Chess-Checkers" + name);
	frame.pack();
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	while (!areImagesLoaded()) {
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	frame.setVisible(true);
    }

    public static void setup() {
	offerSurrender = false;
	gameOver = false;
	if (!setup.getButtonClosed())
	    System.exit(0);
	if (setup.humans.isButton1Selected()) {
	    // VS AI, Does nothing special right now
	    client = new Server(setup.black.isButton1Selected(), !setup.game.isButton1Selected(), true);
	    ((Server) client).startServer(11778);
	} else {
	    if (!setup.internet.isButton1Selected()) {
		// Local server
		client = new Server(setup.black.isButton1Selected(), !setup.game.isButton1Selected(), true);
		((Server) client).startServer(11778);
	    } else {
		if (setup.host.isButton1Selected()) {
		    // Host a server
		    client = new Server(setup.black.isButton1Selected(), !setup.game.isButton1Selected(), false);
		    ((Server) client).startServer(Integer.valueOf(setup.port.text.getText()));
		} else {
		    // Connect to remote server
		    client = new Client(setup.black.isButton1Selected(), !setup.game.isButton1Selected(), false);
		    client.start(setup.ip.text.getText(), Integer.valueOf(setup.port.text.getText()));
		}
	    }
	}
    }

    public static synchronized boolean areImagesLoaded() {
	return imagesLoaded;
    }

    public static synchronized void setImagesLoaded(boolean loaded) {
	imagesLoaded = loaded;
    }

    public static class ClientTimeoutException extends Exception {
	private static final long serialVersionUID = -4472530227066677609L;
    }
}
