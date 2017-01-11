package test;

import dam.Control.CheckersGame;
import dam.menus.mainMenu;

/**
 * Created by Emil Damsbo on 09-01-2017.
 */
public class Launcher {

    public static void main(String[] args) {
        mainMenu dialog = new mainMenu();
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        CheckersGame game = new CheckersGame(Integer.parseInt(args[0]));
    }
}
