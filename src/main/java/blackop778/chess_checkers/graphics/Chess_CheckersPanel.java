package blackop778.chess_checkers.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.chess.Snapshot;
import blackop778.chess_checkers.chess.SnapshotStorage;

@SuppressWarnings("serial")
public class Chess_CheckersPanel extends JPanel {
    private final JPanel game;
    private final JPanel hudContainer;

    public Chess_CheckersPanel() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	hudContainer = new JPanel();
	JButton surrender = new JButton("Offer Surrender");
	surrender.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		Chess_Checkers.offerSurrender = true;
	    }
	});
	hudContainer.add(surrender);
	add(hudContainer);
	game = new JPanel() {
	    @Override
	    protected void paintComponent(Graphics g) {
		for (int i = 0; i < 8; i++) {
		    for (int n = 0; n < 8; n++) {
			if ((i + n) % 2 == 1) {
			    g.setColor(new Color(119, 215, 247));
			} else {
			    g.setColor(Color.WHITE);
			}
			g.fillRect(i * 90, n * 90, (i + 1) * 90, (n + 1) * 90);
			if (Chess_Checkers.client.getBoard()[i][n] != null) {
			    Chess_Checkers.client.getBoard()[i][n].drawSelf(g, i * 90, n * 90);
			}
		    }
		}
	    }
	};
	game.setPreferredSize(new Dimension(720, 720));
	game.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (Chess_Checkers.gameOver) {
		    Chess_Checkers.setup();
		} else {
		    int x = e.getX() / 90;
		    int y = e.getY() / 90;
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
			if (!Chess_Checkers.gameOver && Chess_Checkers.offerSurrender) {
			    String color;
			    // Client hasn't switched yet on local games
			    if (!Chess_Checkers.client.getBlack()) {
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
				    winner = Chess_Checkers.client.getBlack() ? "Black" : "Red";
				} else {
				    winner = Chess_Checkers.client.getBlack() ? "Black" : "White";
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
	    }
	});
	add(game);
    }
}
