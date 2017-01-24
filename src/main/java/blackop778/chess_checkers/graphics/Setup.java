package blackop778.chess_checkers.graphics;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Setup {
    public ArrayList<Options> os = new ArrayList<Options>();

    public Setup() {
	JDialog dialog = new JDialog();
	JPanel big = new JPanel();
	big.setLayout(new BoxLayout(big, BoxLayout.Y_AXIS));
	dialog.add(big);
	big.add(new JLabel("Chess-Checkers"));
	os.add(new Options("Game:", "Chess", "Checkers", false));
	big.add(os.get(0));
	os.add(new Options("Number of humans:", "1", "2", false));
	big.add(os.get(1));
	dialog.setTitle("Chess-Checkers Setup");
	dialog.pack();
	dialog.setResizable(false);
	dialog.setLocationRelativeTo(null);
	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	dialog.setVisible(true);
    }

    public class Options extends JPanel {
	private static final long serialVersionUID = -1954244826051963946L;

	public Options(String label, String b1Label, String b2Label, boolean firstSelected) {
	    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	    add(new JLabel(label));
	    ButtonGroup bg = new ButtonGroup();
	    JRadioButton b1 = new JRadioButton(b1Label, firstSelected);
	    bg.add(b1);
	    add(b1);
	    JRadioButton b2 = new JRadioButton(b2Label, !firstSelected);
	    bg.add(b2);
	    add(b2);
	}
    }
}
