package blackop778.chess_checkers.graphics;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import blackop778.chess_checkers.net.HandshakeMessage;

public class ColorConflictCorrection extends JDialog {
    private static final long serialVersionUID = -3907485484317650006L;
    private final JPanel panel;
    private final JRadioButton random;
    private final JRadioButton rps;
    private HandshakeMessage msg;

    public ColorConflictCorrection(HandshakeMessage msg) {
	super((JDialog) null);
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
	panel.add(enter);
	pack();
    }

    public JRadioButton getRandom() {
	return random;
    }

    public JRadioButton getRPS() {
	return rps;
    }
}
