package dam.menus;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by smous on 11-01-2017.
 */

public class mainMenu extends JDialog {
    private JPanel contentPane;
    private JButton buttonNewGame;
    private JButton buttonSettings;
    private JButton buttonExit;
    private JButton buttonHighScore;
    private JButton buttonRules;
    private JLabel labelWelcome;

    public mainMenu(GameSetup setup) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonNewGame);

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
        buttonHighScore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { onHighScore();
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

    private void onNewGame() {
        // add your code here
        dispose();
    }

    private void onSettings(GameSetup setup) {
        Settings settingsDialog = new Settings(setup);
        settingsDialog.pack();
        settingsDialog.setResizable(false);
        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.setVisible(true);

    }

    private void onRules() {
        Rules rulesDialog = new Rules();
        rulesDialog.pack();
        rulesDialog.setResizable(false);
        rulesDialog.setLocationRelativeTo(null);
        rulesDialog.setVisible(true);
    }

    private void onHighScore() {
        // add your code here if necessary

    }

    private void onExit() {
        // add your code here if necessary
        System.exit(0);
    }


}
