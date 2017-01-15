package dam.menus;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

/**
 * Created by smous on 11-01-2017.
 * Member primarily responsible for file: Nicklas Juul
 */

public class MenuSettings extends JDialog {
    // fields
    private JPanel contentPane;
    private JPanel JPanelOptionsSize;
    private JLabel labelSettingsHeader;
    private JLabel labelSetSize;
    private JLabel labelBoardSize;
    private JButton buttonExit;
    private JRadioButton radioMinSize;
    private JRadioButton radioMedSize;
    private JRadioButton radioMaxSize;
    private JSlider sliderBoardSize;
    private final GameSetup setup;

    // constructor
    public MenuSettings(GameSetup setup) {
        this.setup = setup;

        // adding the radio buttons to a ButtonGroup to make only one of them selected
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(radioMinSize);
        radioGroup.add(radioMedSize);
        radioGroup.add(radioMaxSize);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonExit);

        // adding Actionlistener to all the buttons
        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        });
        radioMinSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onMinSize();
            }
        });
        radioMedSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onMedSize();
            }
        });
        radioMaxSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onMaxSize();
            }
        });
        sliderBoardSize.addChangeListener(new ChangeListener() {
            // sets the number of squares to the slider value when it's not adjusting.
            public void stateChanged(ChangeEvent e) {
                e.getSource();
                if(!sliderBoardSize.getValueIsAdjusting()) {
                    setup.boardSize = sliderBoardSize.getValue();
                }

            }
        });

        // call onExit() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        // call onExit() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onExit();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    // sets frame size to minimum size when clicked on min button
    private void onMinSize() {
        setup.frameSize = GameSetup.FrameSize.MIN_SIZE;

    }
    // sets frame size to medium size when clicked on med button
    private void onMedSize() {
        setup.frameSize = GameSetup.FrameSize.MED_SIZE;

    }
    // sets frame size to maximum size when clicked on max button
    private void onMaxSize() {
        setup.frameSize = GameSetup.FrameSize.MAX_SIZE;

    }

    private void onExit() {
        dispose();
    }
}
