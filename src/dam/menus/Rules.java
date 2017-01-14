package dam.menus;

import javax.swing.*;
import java.awt.event.*;
/**
 * Created by smous on 11-01-2017.
 * Member primarily responsible for file: Nicklas Juul
 */

public class Rules extends JDialog {
    // fields
    private JPanel contentPane;
    private JButton buttonExit;
    private JLabel labelHeader;
    private JTextArea textRules;

    // rules constructor
    public Rules() {
        // the content panel contains a text area with rules
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonExit);
        textRules.setWrapStyleWord(true); // this sets the text wrapping to wrap at words rather than characters

        // added actionlistener to exit button
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
