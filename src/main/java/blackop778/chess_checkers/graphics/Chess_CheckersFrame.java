package blackop778.chess_checkers.graphics;

import javax.swing.JFrame;

import blackop778.chess_checkers.Chess_Checkers;

@SuppressWarnings("serial")
public class Chess_CheckersFrame extends JFrame {
    public Chess_CheckersFrame() {
	Chess_Checkers.panel = new Chess_CheckersPanel();
	add(Chess_Checkers.panel);
    }
}
