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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.chess.Snapshot;
import blackop778.chess_checkers.chess.SnapshotStorage;

@SuppressWarnings("serial")
public class Chess_CheckersPanel extends JPanel {
    private final JPanel gameContainer;
    private final JPanel game;
    private final JScrollPane scroll;
    private final JPanel hudContainer;
    private final JLabel turn;
    private final JTextArea moves;

    public Chess_CheckersPanel() {
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	hudContainer = new JPanel();
	JButton surrender = new JButton("Offer Surrender");
	surrender.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (Chess_Checkers.client.getTurn()) {
		    Chess_Checkers.client.offer("(=+)");
		}
	    }
	});
	hudContainer.add(surrender);
	JButton draw = new JButton("Propose Draw");
	draw.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (Chess_Checkers.client.getTurn()) {
		    Chess_Checkers.client.offer("(=)");
		}
	    }
	});
	hudContainer.add(draw);
	turn = new JLabel();
	hudContainer.add(turn);
	moves = new JTextArea();
	moves.setEditable(false);
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
		    int x = (int) Math.floor(e.getX() / 90.0);
		    int y = (int) Math.floor(e.getY() / 90.0);
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
	    }
	});
	gameContainer = new JPanel();
	gameContainer.add(game);
	scroll = new JScrollPane();
	gameContainer.add(scroll);
	scroll.setViewportView(moves);
	scroll.setPreferredSize(new Dimension(140, 720));
	updateHUD();
	add(gameContainer);
    }

    public void updateHUD() {
	// Multiplayer over internet
	if (Chess_Checkers.setup.internet.isButton1Selected()) {
	    if (Chess_Checkers.client.getTurn()) {
		if (Chess_Checkers.setup.whiteName.getText().equals("Your"))
		    turn.setText(Chess_Checkers.setup.whiteName.getText() + " turn");
		else
		    turn.setText(Chess_Checkers.setup.whiteName.getText() + "'s turn");
	    } else {
		turn.setText(Chess_Checkers.setup.blackName.getText() + "'s turn");
	    }
	    // Local
	} else {
	    if (Chess_Checkers.client.getTurn()) {
		if (Chess_Checkers.client.getBlack()) {
		    turn.setText(Chess_Checkers.setup.blackName.getText() + "'s turn");
		} else {
		    turn.setText(Chess_Checkers.setup.whiteName.getText() + "'s turn");
		}
	    } else {
		if (Chess_Checkers.client.getBlack()) {
		    turn.setText(Chess_Checkers.setup.whiteName.getText() + "'s turn");
		} else {
		    turn.setText(Chess_Checkers.setup.blackName.getText() + "'s turn");
		}
	    }
	}

	moves.setText(Chess_Checkers.client.getNotation());
    }
}
