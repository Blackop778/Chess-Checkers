package blackop778.chess_checkers;

import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

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
    private static String notation;
    // If we need to start a new notation line
    private static boolean newLine;
    private static int turns;
    public static Setup setup;
    public static final boolean DISABLE_AI = true;
    public static final boolean DISABLE_INTERNET = false;
    private static boolean imagesLoaded;
    public static long ourSeed;
    public static final boolean LOG_TO_FILE = false;
    public static final boolean IMAGE_LOAD_TROUBLE = false;
    public static final boolean DEBUG_LOGGING = true;

    public static void main(String[] args) {
	int index = 0;
	if (LOG_TO_FILE) {
	    File output = new File("LogOutput.txt");
	    while (output.exists()) {
		index++;
		output = new File("LogOutput" + index + ".txt");
	    }
	    PrintStream out = null;
	    try {
		out = new PrintStream(new FileOutputStream(output));
	    } catch (FileNotFoundException e1) {
		e1.printStackTrace();
		System.exit(2);
	    }
	    System.setOut(out);
	    System.setErr(out);
	}
	imagesLoaded = false;
	if (IMAGE_LOAD_TROUBLE) {
	    Timer t = new Timer();
	    t.schedule(new TimerTask() {
		@Override
		public void run() {
		    if (!areImagesLoaded()) {
			System.out.println("Images failed to load after 10s, terminating");
			System.exit(3);
		    }
		}
	    }, 10000);
	}
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
	debugLog("Starting and images loaded");
	frame.setVisible(true);
    }

    public static void setup() {
	if (setup == null) {
	    setup = new Setup();
	} else {
	    setup.redisplay();
	}
	offerSurrender = false;
	gameOver = false;
	notation = "";
	turns = 0;
	newLine = true;
	if (!setup.getButtonClosed())
	    System.exit(0);
	if (setup.humans.isButton1Selected()) {
	    // VS AI, Does nothing special right now
	    client = new Server(setup.black.isButton1Selected(), !setup.game.isButton1Selected(), true);
	    ((Server) client).startServer(11778);
	} else {
	    if (!setup.internet.isButton1Selected()) {
		// Local server
		if (setup.game.isButton1Selected()) {
		    client = new Server(setup.black.isButton1Selected(), !setup.game.isButton1Selected(), true);
		    ((Server) client).startServer(11778);
		} else {
		    client = new Server(!setup.black.isButton1Selected(), !setup.game.isButton1Selected(), true);
		    ((Server) client).startServer(11778);
		}
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

    public static void debugLog(String message) {
	if (DEBUG_LOGGING) {
	    System.out.println("[" + System.currentTimeMillis() + "] " + message);
	}
    }

    public static class ClientTimeoutException extends Exception {
	private static final long serialVersionUID = -4472530227066677609L;
    }

    public static String getNotation() {
	return notation;
    }

    public static void updateNotation(String toAdd) {
	if (newLine) {
	    notation += (turns + 1) + ".";
	}
	notation += " " + toAdd;
	if (!newLine) {
	    notation += "\n";
	    turns++;
	}

	newLine = !newLine;
    }
}
