package blackop778.chess_checkers.graphics;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Chess_CheckersPanel extends JPanel
{
	public Chess_CheckersPanel()
	{
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{

			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{

	}
}
