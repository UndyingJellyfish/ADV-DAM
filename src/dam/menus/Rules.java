package dam.menus;

import javax.swing.*;
import java.awt.event.*;

public class Rules extends JDialog {
    private JPanel contentPane;
    private JButton buttonExit;
    private JLabel labelHeader;
    private JTextArea textRules;

    public Rules() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonExit);

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });

        // call onCancel() when cross is clicked
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

    private void onExit() {
        // add your code here if necessary
        dispose();
    }
}
