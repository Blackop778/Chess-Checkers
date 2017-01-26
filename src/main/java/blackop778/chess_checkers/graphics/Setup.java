package blackop778.chess_checkers.graphics;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Setup {
    public final Option game;
    public final Option humans;
    public final Option black;
    public final Option internet;
    public final Option host;
    public final Text ip;
    public final Text port;
    public final JButton enter;
    public final JPanel panel;
    public final JDialog dialog;
    private boolean setup;

    @SuppressWarnings("serial")
    public Setup() {
	setup = false;
	dialog = new JDialog();
	panel = new JPanel() {
	    // Ghetto solution to keep enter button on bottom
	    @Override
	    public Component add(Component comp) {
		if (setup) {
		    super.add(comp);
		    remove(enter);
		    super.add(enter);
		} else
		    super.add(comp);

		return comp;
	    }
	};
	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	dialog.add(panel);
	game = new Option("Game:", "Chess", "Checkers", true);
	panel.add(game);
	black = new Option("Play as black:", "Yes", "No", false);
	humans = new Option("Number of humans:", "1", "2", false, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		internet.removeCustom(panel, dialog);
		black.addCustom(panel, dialog);
	    }
	}, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		internet.addCustom(panel, dialog);
		black.removeCustom(panel, dialog);
	    }
	});
	panel.add(humans);
	ip = new Text("IP address:", 4);
	port = new Text("Port:", 4);
	host = new Option("Host the game:", "Yes", "No", true, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		panel.remove(ip);
		panel.repaint();
		dialog.pack();
	    }
	}, new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		panel.add(ip);
		panel.repaint();
		dialog.pack();
	    }
	}, new Component[] { port }, new Component[] { ip, port });
	internet = new Option("Multiplayer over internet:", "Yes", "No", false, new ActionListener() {
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
	enter = new JButton("Enter");
	panel.add(enter);
	setup = true;
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
    public class Option extends JPanel {
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
	public Option(String label, String label1, String label2, boolean firstSelected) {
	    this(label, label1, label2, firstSelected, null, null);
	}

	public Option(String label, String label1, String label2, boolean firstSelected, ActionListener al1,
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
	public Option(String label, String label1, String label2, boolean firstSelected, ActionListener al1,
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
		if (c instanceof Option) {
		    Option o = (Option) c;
		    o.addCustom(container, window);
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
		if (c instanceof Option) {
		    Option o = (Option) c;
		    o.removeCustom(container, window);
		} else
		    container.remove(c);
	    }
	    container.repaint();
	    window.pack();
	}
    }

    public class Text extends JPanel {
	private static final long serialVersionUID = -1712072660292711164L;
	public final JTextField text;

	public Text(String label, int textFieldWidth) {
	    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	    add(new JLabel(label));
	    text = new JTextField(textFieldWidth);
	    text.setEditable(true);
	    add(text);
	}
    }
}
