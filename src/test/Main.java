package test;

import dam.Control.CheckersGame;
import dam.menus.test;


/**
 * Created by Emil Damsbo on 09-01-2017.
 */
public class Main {

    public static void main(String[] args) {
        test dialog = new test();
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        CheckersGame game = new CheckersGame(Integer.parseInt(args[0]));
    }


}
