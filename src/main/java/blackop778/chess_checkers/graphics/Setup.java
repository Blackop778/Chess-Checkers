package blackop778.chess_checkers.graphics;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Setup {
    public final Options game;
    public final Options humans;
    public final Options internet;
    public final Options host;
    public final JPanel panel;
    public final JDialog dialog;

    public Setup() {
	dialog = new JDialog();
	panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	dialog.add(panel);
	game = new Options("Game:", "Chess", "Checkers", true);
	panel.add(game);
	humans = new Options("Number of humans:", "1", "2", false, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		internet.removeCustom(panel, dialog);
	    }
	}, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		internet.addCustom(panel, dialog);
	    }
	});
	panel.add(humans);
	host = new Options("Host the game:", "yes", "no", true);
	internet = new Options("Multiplayer over internet:", "yes", "no", false, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		host.addCustom(panel, dialog);
	    }

	}, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		host.removeCustom(panel, dialog);
	    }

	}, new Component[] { host }, new Component[0]);
	internet.addCustom(panel, dialog);
	dialog.setTitle("Chess-Checkers Setup");
	dialog.pack();
	dialog.setResizable(false);
	dialog.setLocationRelativeTo(null);
	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	dialog.setVisible(true);
    }

    /**
     * Designed to be pretty generic but I decided to add some specifics instead
     * of having subclass hell in Setup
     */
    public class Options extends JPanel {
	private static final long serialVersionUID = -1954244826051963946L;

	public final ButtonGroup bg;
	public final String label1;
	public final String label2;
	// Added when first button is selected
	public final Component[] o1Next;
	// Added when second button is selected
	public final Component[] o2Next;

	/**
	 * Adds no action listeners
	 * 
	 * @param label
	 * @param b1Label
	 * @param b2Label
	 * @param firstSelected
	 */
	public Options(String label, String label1, String label2, boolean firstSelected) {
	    this(label, label1, label2, firstSelected, null, null);
	}

	public Options(String label, String label1, String label2, boolean firstSelected, ActionListener al1,
		ActionListener al2) {
	    this(label, label1, label2, firstSelected, al1, al2, new Component[0], new Component[0]);
	}

	/**
	 * 
	 * @param label
	 * @param label1
	 * @param label2
	 * @param firstSelected
	 * @param al1
	 * @param al2
	 * @param o1Next
	 *            Don't make null, make empty
	 * @param o2Next
	 *            Don't make null, make empty
	 */
	public Options(String label, String label1, String label2, boolean firstSelected, ActionListener al1,
		ActionListener al2, Component[] o1Next, Component[] o2Next) {
	    this.label1 = label1;
	    this.label2 = label2;
	    this.o1Next = o1Next;
	    this.o2Next = o2Next;
	    if (o1Next == null)
		throw new NullPointerException("o1Next be null, will cause headaches");
	    if (o2Next == null)
		throw new NullPointerException("o2Next be null, will cause headaches");
	    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	    add(new JLabel(label));
	    bg = new ButtonGroup();
	    JRadioButton b1 = new JRadioButton(label1, firstSelected);
	    b1.setActionCommand(label1);
	    if (al1 != null)
		b1.addActionListener(al1);
	    bg.add(b1);
	    add(b1);
	    JRadioButton b2 = new JRadioButton(label2, !firstSelected);
	    b2.setActionCommand(label2);
	    if (al2 != null)
		b2.addActionListener(al2);
	    bg.add(b2);
	    add(b2);
	}

	/**
	 * Allows for additional actions to be performed when added to a
	 * Container
	 * 
	 * @param container
	 */
	public void addCustom(Container container, Window window) {
	    container.add(this);
	    Component[] a;
	    if (bg.getSelection().getActionCommand().equals(label1))
		a = o1Next;
	    else
		a = o2Next;
	    for (Component c : a) {
		if (c instanceof Options) {
		    Options o = (Options) c;
		    o.add(container);
		} else
		    container.add(c);
	    }
	    container.repaint();
	    window.pack();
	}

	/**
	 * Allows for additional actions to be performed when removed from a
	 * Container
	 * 
	 * @param container
	 */
	public void removeCustom(Container container, Window window) {
	    container.remove(this);
	    Component[] a;
	    if (bg.getSelection().getActionCommand().equals(label1))
		a = o1Next;
	    else
		a = o2Next;
	    for (Component c : a) {
		if (c instanceof Options) {
		    Options o = (Options) c;
		    o.remove(container);
		} else
		    container.remove(c);
	    }
	    container.repaint();
	    window.pack();
	}
    }
}
