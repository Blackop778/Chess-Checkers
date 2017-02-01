package blackop778.chess_checkers.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ColorChooser extends JDialog {
    private static final long serialVersionUID = -2937891388510429098L;
    private boolean black;
    private boolean buttonClosed;

    public ColorChooser() {
	super((JDialog) null);
	black = true;
	buttonClosed = false;
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	JPanel subPanel = new JPanel();
	subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.X_AXIS));
	ButtonGroup bg = new ButtonGroup();
	JRadioButton b1 = new JRadioButton("White", true);
	b1.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		black = true;
	    }
	});
	bg.add(b1);
	subPanel.add(b1);
	JRadioButton b2 = new JRadioButton("Black", false);
	b2.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		black = false;
	    }
	});
	bg.add(b2);
	subPanel.add(b2);
	panel.add(subPanel);
	JButton enter = new JButton("Enter");
	enter.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		buttonClosed = true;
		dispose();
	    }
	});
	panel.add(enter);
	add(panel);
	setTitle("Choose your color");
	pack();
	setResizable(false);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setModal(true);
	setVisible(true);
    }

    public boolean getBlack() {
	return new Boolean(black);
    }

    public boolean getButtonClosed() {
	return new Boolean(buttonClosed);
    }
}
