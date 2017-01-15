package dam.menus;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by smous on 11-01-2017.
 * Member primarily responsible for file: Nicklas Juul
 */
// main menu is the opening screen when you run the program. In this menu you can choose between some buttons which opens
// new dialogs
public class MenuMain extends JDialog {
    private JPanel contentPane;
    private JButton buttonNewGame;
    private JButton buttonSettings;
    private JButton buttonExit;
    private JButton buttonRules;
    private JLabel labelWelcome;

    // main menu constructor with the game setup as argument to access the variables
    public MenuMain(GameSetup setup) {
        setContentPane(contentPane);
        // setModal to true so the checkers board will not open with main menu
        setModal(true);
        getRootPane().setDefaultButton(buttonNewGame);

        // adding Actionslisteners to all buttons.
        buttonNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onNewGame();
            }
        });

        buttonSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSettings(setup);
            }
        });
        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onExit();
            }
        });
        buttonRules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onRules();
            }
        });


        // call onExit() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);



    }
    // closes the menu when clicking on new game, then opens the board
    private void onNewGame() {
        // add your code here
        dispose();
    }

    // opens a new dialog when clicked on settings. The method has game setup as argument to keep access to the arguments
    // in that class
    private void onSettings(GameSetup setup) {
        MenuSettings settingsDialog = new MenuSettings(setup);
        settingsDialog.pack();
        settingsDialog.setResizable(false);
        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.setTitle("Main Menu -> Settings");
        settingsDialog.setVisible(true);

    }

    // opens a new dialog when clicked on rules.
    private void onRules() {
        MenuRules rulesDialog = new MenuRules();
        rulesDialog.pack();
        rulesDialog.setResizable(false);
        rulesDialog.setLocationRelativeTo(null);
        rulesDialog.setTitle("Main Menu -> Game rules");
        rulesDialog.setVisible(true);
    }

    // shuts down program when clicked on exit button
    private void onExit() {
        // add your code here if necessary
        System.exit(0);
    }


}
