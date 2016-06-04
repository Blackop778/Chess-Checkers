package blackop778.chess_checkers.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import blackop778.chess_checkers.Chess_Checkers;

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
				int x = e.getX() / 90;
				int y = e.getY() / 90;
				if(Chess_Checkers.board[x][y] != null)
				{
					if(!Chess_Checkers.board[x][y].selected)
						Chess_Checkers.board[x][y].select(x, y);
					else
						Chess_Checkers.unselectAll();
				}
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		for(int i = 0; i < 8; i++)
		{
			for(int n = 0; n < 8; n++)
			{
				if((i + n) % 2 == 1)
					g.setColor(new Color(119, 215, 247));
				else
					g.setColor(Color.WHITE);
				g.fillRect(i * 90, n * 90, (i + 1) * 90, (n + 1) * 90);
				if(Chess_Checkers.board[i][n] != null)
					Chess_Checkers.board[i][n].drawSelf(g, i * 90, n * 90);
			}
		}
	}
}
