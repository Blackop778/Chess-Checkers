package blackop778.chess_checkers.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.chess.Snapshot;
import blackop778.chess_checkers.chess.SnapshotStorage;
import blackop778.chess_checkers.pieces.ChessPiece;

@SuppressWarnings("serial")
public class Chess_CheckersPanel extends JPanel {
    public Chess_CheckersPanel() {
	addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (Chess_Checkers.gameOver) {
		    Chess_Checkers.setup();
		} else if (e.getY() <= 45) {
		    if (e.getX() >= 200 && e.getX() <= 405) {
			repaint();
		    }
		} else {
		    int x = e.getX() / 90;
		    int y = (e.getY() - 45) / 90;
		    if (Chess_Checkers.client.getBoard()[x][y].possible) {
			Chess_Checkers.client.getBoard()[x][y].selector.move(x, y);
			if (!Chess_Checkers.client.gameIsCheckers) {
			    if (SnapshotStorage.addSnapshot(new Snapshot())) {
				Chess_Checkers.gameOver = true;
				JOptionPane.showMessageDialog(null,
					"Threefold repitition has occured, and the game is"
						+ " a draw. Click on the board after exiting this message to start a new game.",
					"Deadlock has been reached", JOptionPane.INFORMATION_MESSAGE);
			    }
			}
		    } else {
			if (!Chess_Checkers.client.getBoard()[x][y].selected) {
			    Chess_Checkers.client.getBoard()[x][y].select(x, y);
			} else {
			    Chess_Checkers.client.unselectAll();
			    Chess_Checkers.client.getBoard()[x][y].selected = false;
			}
		    }
		}
		repaint();
		if (Chess_Checkers.offerSurrender) {
		    String color;
		    if (!Chess_Checkers.client.getTurn()) {
			color = "Black";
		    } else if (Chess_Checkers.client.gameIsCheckers) {
			color = "Red";
		    } else {
			color = "White";
		    }
		    int response = JOptionPane.showConfirmDialog(null,
			    color + " has offered to surrender. Do you accept?", "", JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.OK_OPTION) {
			Chess_Checkers.gameOver = true;
			String winner;
			if (Chess_Checkers.client.gameIsCheckers) {
			    winner = Chess_Checkers.client.getTurn() ? "Black" : "Red";
			} else {
			    winner = Chess_Checkers.client.getTurn() ? "Black" : "White";
			}
			JOptionPane.showMessageDialog(null,
				"Congratulations, " + winner
					+ " wins. Exit this message and click on the board to restart.",
				"A Champion has been decided!", JOptionPane.INFORMATION_MESSAGE);
		    } else {
			Chess_Checkers.offerSurrender = false;
			repaint();
		    }
		}
	    }
	});

    }

    @Override
    protected void paintComponent(Graphics g) {
	g.setColor(Color.LIGHT_GRAY);
	g.fillRect(0, 0, 721, 45);
	g.setColor(Color.BLACK);
	g.setFont(Chess_Checkers.font.deriveFont(30f));
	g.drawString("Player turn: ", 10, 35);
	if (Chess_Checkers.client.getTurn() && Chess_Checkers.client.black) {
	    g.setColor(Color.BLACK);
	} else {
	    if (Chess_Checkers.client.gameIsCheckers && !Chess_Checkers.client.black) {
		g.setColor(Color.RED);
	    } else {
		g.setColor(Color.WHITE);
	    }
	}
	g.fillRect(150, 5, 30, 30);
	if (!Chess_Checkers.offerSurrender) {
	    g.setFont(Chess_Checkers.fontBold.deriveFont(30f));
	    g.setColor(Color.BLACK);
	    g.drawString("Offer Surrender", 200, 35);
	    g.drawRect(200, 1, 205, 43);
	}
	if (!Chess_Checkers.client.gameIsCheckers) {
	    g.setColor(Color.BLACK);
	    g.setFont(Chess_Checkers.font.deriveFont(30f));
	    g.drawString("Check: ", 415, 35);
	    if (ChessPiece.isKingInCheck(Chess_Checkers.client.black)) {
		if (!ChessPiece.canMove(Chess_Checkers.client.black)) {
		    g.setColor(new Color(218, 165, 32));
		    g.setFont(Chess_Checkers.fontBold.deriveFont(30f));
		    g.drawString("MATE", 507, 35);
		} else {
		    g.setColor(Color.RED);
		    g.drawString("true", 507, 35);
		}
	    } else {
		g.drawString("false", 507, 35);
	    }
	}
	for (int i = 0; i < 8; i++) {
	    for (int n = 0; n < 8; n++) {
		if ((i + n) % 2 == 1) {
		    g.setColor(new Color(119, 215, 247));
		} else {
		    g.setColor(Color.WHITE);
		}
		g.fillRect(i * 90, n * 90 + 45, (i + 1) * 90, (n + 1) * 90);
		if (Chess_Checkers.client.getBoard()[i][n] != null) {
		    Chess_Checkers.client.getBoard()[i][n].drawSelf(g, i * 90, n * 90 + 45);
		}
	    }
	}
    }
}
