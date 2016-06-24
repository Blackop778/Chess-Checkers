package blackop778.chess_checkers.chess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class PawnPromotion extends JPanel implements ActionListener
{
	public PawnPromotion()
	{
		super(new BorderLayout());

		ButtonGroup group = new ButtonGroup();

		JRadioButton queen = new JRadioButton("Queen", true);
		queen.addActionListener(this);
		queen.setActionCommand("Queen");
		queen.setMnemonic(1);
		group.add(queen);

		JRadioButton rook = new JRadioButton("Rook", false);
		rook.addActionListener(this);
		rook.setActionCommand("Rook");
		rook.setMnemonic(1);
		group.add(rook);

		JRadioButton knight = new JRadioButton("Knight", false);
		knight.addActionListener(this);
		knight.setActionCommand("Knight");
		knight.setMnemonic(1);
		group.add(knight);

		JRadioButton bishop = new JRadioButton("Bishop", false);
		bishop.addActionListener(this);
		bishop.setActionCommand("Bishop");
		bishop.setMnemonic(1);
		group.add(bishop);

		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		buttonPanel.add(queen);
		buttonPanel.add(rook);
		buttonPanel.add(knight);
		buttonPanel.add(bishop);

		JButton done = new JButton("Done");
		done.addActionListener(this);
		done.setActionCommand("Done");
		done.setMnemonic(1);

		JLabel text = new JLabel();
		text.setText("Pick what piece you want your pawn to be promoted to.");

		add(text, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.WEST);
		add(done, BorderLayout.SOUTH);
	}

	public static void showDialog()
	{
		JFrame frame = new JFrame("Promotion");

		JComponent component = new PawnPromotion();
		component.setOpaque(true);
		frame.setContentPane(component);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{

	}
}
