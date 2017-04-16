package blackop778.chess_checkers.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import blackop778.chess_checkers.Chess_Checkers;
import blackop778.chess_checkers.net.HandshakeMessage;

public class ColorConflictCorrection {
    private final JDialog dialog;
    private final JPanel panel;
    private final JRadioButton random;
    private final JRadioButton rps;
    private final HandshakeMessage msg;
    private final ColorConflictCorrection _this;

    public ColorConflictCorrection(HandshakeMessage msg) {
	dialog = new JDialog((JDialog) null);
	_this = this;
	this.msg = msg;
	panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	panel.add(new JTextField(
		"Both players tried to choose the same color, how would you like to decide who's what color?"));
	JPanel bp = new JPanel();
	ButtonGroup buttons = new ButtonGroup();
	random = new JRadioButton("Randomly", true);
	buttons.add(random);
	bp.add(random);
	rps = new JRadioButton("Rock Paper Scissors");
	buttons.add(rps);
	bp.add(rps);
	panel.add(bp);
	JButton enter = new JButton("Enter");
	enter.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		dialog.setVisible(false);
		Chess_Checkers.client.resolveColorConflict(_this);
	    }
	});
	panel.add(enter);
	dialog.add(panel);
	dialog.pack();
	dialog.setVisible(true);
    }

    public JRadioButton getRandom() {
	return random;
    }

    public JRadioButton getRPS() {
	return rps;
    }

    public HandshakeMessage getMSG() {
	return msg;
    }
}
