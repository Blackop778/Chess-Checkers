package blackop778.chess_checkers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import blackop778.chess_checkers.graphics.Chess_CheckersFrame;
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

    public static void main(String[] args) {
	try {
	    font = Font.createFont(Font.TRUETYPE_FONT, new File("resources" + File.separator + "FreeSans.otf"));
	    fontBold = Font.createFont(Font.TRUETYPE_FONT, new File("resources" + File.separator + "FreeSansBold.otf"));
	} catch (FontFormatException | IOException e) {
	    e.printStackTrace();
	}

	setup();
    }

    public static void startGUI() {
	Chess_CheckersFrame frame = new Chess_CheckersFrame();
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
	Setup s = new Setup();
	if (s.buttonClosed) {
	    if (s.humans.button1Selected()) {
		// Does nothing special right now
		client = new Server(s.black.button1Selected(), !s.game.button1Selected(), true);
		((Server) client).startServer(11778);
	    } else {
		if (!s.internet.button1Selected()) {
		    // Local server
		    client = new Server(s.black.button1Selected(), !s.game.button1Selected(), true);
		    ((Server) client).startServer(11778);
		} else {
		    if (s.host.button1Selected()) {
			// Host a server
			client = new Server(s.black.button1Selected(), !s.game.button1Selected(), false);
			((Server) client).startServer(Integer.valueOf(s.port.text.getText()));
		    } else {
			// Connect to remote server
			client = new Client(s.black.button1Selected(), !s.game.button1Selected(), false);
			client.start(s.ip.text.getText(), Integer.valueOf(s.port.text.getText()));
		    }
		}
	    }
	}
    }

    public static class ClientTimeoutException extends Exception {
	private static final long serialVersionUID = -4472530227066677609L;
    }
}
