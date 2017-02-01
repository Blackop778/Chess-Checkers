package blackop778.chess_checkers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsConfiguration;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import blackop778.chess_checkers.graphics.Chess_CheckersPanel;
import blackop778.chess_checkers.graphics.Setup;
import blackop778.chess_checkers.net.Client;
import blackop778.chess_checkers.net.Server;

public abstract class Chess_Checkers {
    public static Client client;
    public static Client clientPartner;
    public static boolean offerSurrender;
    public static boolean gameOver;
    public static Chess_CheckersPanel panel;
    public static Font font;
    public static Font fontBold;
    public static String notation;
    public static Setup setup;

    public static void main(String[] args) {
	try {
	    font = Font.createFont(Font.TRUETYPE_FONT, new File("resources" + File.separator + "FreeSans.otf"));
	    fontBold = Font.createFont(Font.TRUETYPE_FONT, new File("resources" + File.separator + "FreeSansBold.otf"));
	} catch (FontFormatException | IOException e) {
	    e.printStackTrace();
	}

	setup = new Setup();
	setup();
    }

    public static void startGUI() {
	JFrame frame = new JFrame((GraphicsConfiguration) null);
	panel = new Chess_CheckersPanel();
	frame.add(Chess_Checkers.panel);
	frame.setTitle("Chess-Checkers");
	frame.setPreferredSize(new Dimension(727, 794));
	frame.pack();
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }

    public static void setup() {
	offerSurrender = false;
	gameOver = false;
	if (!setup.getButtonClosed())
	    System.exit(0);
	if (setup.humans.button1Selected()) {
	    // VS AI, Does nothing special right now
	    client = new Server(setup.black.button1Selected(), !setup.game.button1Selected(), true);
	    ((Server) client).startServer(11778);
	} else {
	    if (!setup.internet.button1Selected()) {
		// Local server
		client = new Server(setup.black.button1Selected(), !setup.game.button1Selected(), true);
		((Server) client).startServer(11778);
	    } else {
		if (setup.host.button1Selected()) {
		    // Host a server
		    client = new Server(setup.black.button1Selected(), !setup.game.button1Selected(), false);
		    ((Server) client).startServer(Integer.valueOf(setup.port.text.getText()));
		} else {
		    // Connect to remote server
		    client = new Client(setup.black.button1Selected(), !setup.game.button1Selected(), false);
		    client.start(setup.ip.text.getText(), Integer.valueOf(setup.port.text.getText()));
		}
	    }
	}
    }

    public static class ClientTimeoutException extends Exception {
	private static final long serialVersionUID = -4472530227066677609L;
    }
}
