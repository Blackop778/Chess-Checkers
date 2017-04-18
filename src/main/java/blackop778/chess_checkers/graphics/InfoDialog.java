package blackop778.chess_checkers.graphics;

import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InfoDialog {
    private JFrame frame;
    private JPanel panel;

    public static InfoDialog showMessage(String message) {
	return showMessage(message, "");
    }

    public static InfoDialog showMessage(String message, String title) {
	InfoDialog _this = new InfoDialog();
	_this.frame = new JFrame();
	_this.frame.setTitle(title);
	_this.panel = new JPanel();
	_this.panel.add(new JTextArea(message));
	_this.frame.add(_this.panel);
	_this.frame.setVisible(true);
	_this.frame.pack();

	return _this;
    }

    public static InfoDialog showButtonMessage(String message, ActionListener action, String buttonLabel) {
	return showButtonMessage(message, action, buttonLabel, "");
    }

    public static InfoDialog showButtonMessage(String message, ActionListener action, String buttonLabel,
	    String title) {
	InfoDialog _this = showMessage(message, title);
	_this.panel.setLayout(new BoxLayout(_this.panel, BoxLayout.Y_AXIS));
	JButton but = new JButton(buttonLabel);
	if (action != null)
	    but.addActionListener(action);
	_this.panel.add(but);
	_this.frame.pack();

	return _this;
    }

    public void close() {
	frame.setVisible(false);
	frame.dispose();
    }
}
