package dam.Control;

import dam.abstractions.*;
import dam.graphics.GUIBoard;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Emil Damsbo on 09-01-2017.
 */
public class CheckersGame {
    private int boardSize;
    public static CheckerPiece[][] pieces;

    public CheckersGame(int n) {
        this.boardSize = n;

        // creating players and empty buttons
        IdentifierGenerator idGen = new IdentifierGenerator();
        Player player0 = new Player("Player 0", idGen.getNextIdentifier());
        Player player1 = new Player("Player 1", idGen.getNextIdentifier());
        Player placeholder = new Player("This guy does not exist", -1);
        pieces = new CheckerPiece[boardSize][boardSize];


        // logicBoard creation
        LogicBoard board = new LogicBoard(pieces, boardSize);
        board.populateBoard(player0, player1, placeholder);
        board.printBoard();

        // start loading GUI elements
        GUIBoard graphicsBoard = new GUIBoard(boardSize, board);
        graphicsBoard.fillInAllActionHandlers(); // ensures all buttons act properly when clicked

        // loads window frame, but not visibly
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // needed to ensure proper functionality
        f.add(graphicsBoard, BorderLayout.CENTER); // adds the GUIBoard to the window frame and ensures it's centered

        f.setResizable(false); // disallows window resizeablity, is available in the options menu

        f.pack();
        graphicsBoard.paintBoard(); // loads all textures for game pieces
        f.setLocationRelativeTo(null);

        // finally shows window
        f.setVisible(true);
    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }


}
