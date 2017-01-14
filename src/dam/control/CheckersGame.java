package dam.control;

import dam.abstractions.*;
import dam.graphics.GUIBoard;
import dam.menus.GameSetup;
import javax.swing.*;
import java.awt.*;

/**
 * Created by Emil Damsbo on 09-01-2017.
 * Member primarily responsible for file: Søren Mousten
 */
public class CheckersGame {
    public static boolean continueGame = false;
    public static boolean gameDone = false;
    private JFrame f;

    public CheckersGame(GameSetup setup) {
        int boardSize = setup.boardSquares;


        // creating players and "empty" player
        IdentifierGenerator idGen = new IdentifierGenerator(0);
        Player player0 = new Player("Player 0", idGen.getNextIdentifier());
        Player player1 = new Player("Player 1", idGen.getNextIdentifier());
        Player placeholder = new Player("This guy does not exist", -1);
        CheckerPiece[][] pieces = new CheckerPiece[boardSize][boardSize];

        // logicBoard creation
        LogicBoard board = new LogicBoard(pieces, setup);
        board.PopulateBoard(player0, player1, placeholder);
        board.PrintBoard();

        // start loading GUI elements
        GUIBoard graphicsBoard = new GUIBoard(board, setup);
        graphicsBoard.fillInAllActionHandlers(); // ensures all buttons act properly when clicked

        // loads window frame, but not visibly
        this.f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // needed to ensure proper functionality
        f.add(graphicsBoard, BorderLayout.CENTER); // adds the GUIBoard to the window frame and ensures it's centered

        f.setResizable(false); // disallows window resizability, is available in the options menu

        f.pack();
        graphicsBoard.paintBoard(); // loads all textures for game pieces
        f.setLocationRelativeTo(null);

        // finally shows window
        f.setVisible(true);

    }

    public void closeGame() {
        f.setVisible(false);
        f.dispose();

    }

    public static void infoBox(String infoMessage, String titleBar) {
        Object[] options = {"Back to menu", "Exit game"};

        int input = JOptionPane.showOptionDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
        if(input == JOptionPane.OK_OPTION) {
            continueGame = true;
            gameDone = true;
            return;


        }

        else if (input == JOptionPane.CANCEL_OPTION) {
            continueGame = false;
            gameDone = true;

            return;
        }
        continueGame = false;
        gameDone = true;
    }


}
