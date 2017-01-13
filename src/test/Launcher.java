package test;

import dam.Control.CheckersGame;
import dam.menus.GameSetup;
import dam.menus.mainMenu;

/**
 * Created by Emil Damsbo on 09-01-2017.
 */
public class Launcher {


    public static void main(String[] args) {
        // default boardsize is argument from terminal
        int boardSize = Integer.parseInt(args[0]);
        GameSetup setup = new GameSetup(boardSize);

        // the checkers game continues while game done is false
        do {
            CheckersGame.gameDone = false;
            gameSession(setup);
        }
        while (CheckersGame.continueGame);
    }

    private static void gameSession(GameSetup setup) {
        mainMenu dialog = new mainMenu(setup);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        CheckersGame game = new CheckersGame(setup);

        // while the checkers game is not done, sleep for
        while (!CheckersGame.gameDone) {
            try {
                Thread.sleep(10);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // closes frame
        game.closeGame();

    }

}
