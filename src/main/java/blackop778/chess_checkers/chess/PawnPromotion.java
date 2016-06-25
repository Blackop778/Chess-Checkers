package blackop778.chess_checkers.chess;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PawnPromotion
{
	public String result;

	public PawnPromotion()
	{
		JDialog dialog = new JDialog(null, "Promotion", Dialog.DEFAULT_MODALITY_TYPE);

		JComponent component = new DialogWindow(this, dialog);
		component.setOpaque(true);
		dialog.setContentPane(component);

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
}

@SuppressWarnings("serial")
class DialogWindow extends JPanel implements ActionListener
{
	private PawnPromotion controller;
	private JDialog dialog;

	public DialogWindow(PawnPromotion controller, JDialog dialog)
	{
		super(new BorderLayout());
		this.controller = controller;
		controller.result = "Queen";
		this.dialog = dialog;

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
		text.setText("Pick what piece you want your pawn to be promoted to?");

		add(text, BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.WEST);
		add(done, BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		switch(ae.getActionCommand())
		{
			case "Done":
				dialog.dispose();
				break;
			case "Queen":
				controller.result = "Queen";
				break;
			case "Rook":
				controller.result = "Rook";
				break;
			case "Knight":
				controller.result = "Knight";
				break;
			case "Bishop":
				controller.result = "Bishop";
				break;
		}
	}
}
