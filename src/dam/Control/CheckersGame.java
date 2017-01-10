package dam.Control;

import dam.abstractions.CheckerPiece;
import dam.abstractions.IdentifierGenerator;
import dam.abstractions.LogicBoard;
import dam.abstractions.Player;
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
        // initialization phase
        IdentifierGenerator idGen = new IdentifierGenerator();
        Player player0 = new Player("Player 0", idGen.getNextIdentifier());
        Player player1 = new Player("Player 1", idGen.getNextIdentifier());
        Player placeholder = new Player("This guy does not exist", -1);
        pieces = new CheckerPiece[boardSize][boardSize];


        // board creation
        LogicBoard board = new LogicBoard(pieces, boardSize);
        board.populate(player0, player1, placeholder);
        board.printBoard();


        GUIBoard temp = new GUIBoard(boardSize, board);
        //temp.printButtonArraySize();

        // ensures all buttons act properly when clicked
        temp.fillInAllActionHandlers();
        // graphics loading
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(temp, BorderLayout.CENTER);
        JToolBar tools = new JToolBar();

        f.setResizable(false);

        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

}
