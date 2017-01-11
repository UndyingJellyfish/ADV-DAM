package dam.menus;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

import static dam.menus.GameSetup.FrameSize.MIN_SIZE;

/**
 * Created by smous on 11-01-2017.
 */

public class Settings extends JDialog {
    private JPanel contentPane;
    private JButton buttonExit;
    private JLabel labelSettingsheader;
    private JPanel JPanelOptionsSize;
    private JLabel labelSetSize;
    private JRadioButton radioMinSize;
    private JRadioButton radioMedSize;
    private JRadioButton radioMaxSize;
    private JLabel labelBoardSize;
    private JSlider sliderBoardSize;
    private ButtonGroup RadioGroup = new ButtonGroup();
    private GameSetup setup;


    public Settings(GameSetup setup) {
        this.setup = setup;
        RadioGroup.add(radioMinSize);
        RadioGroup.add(radioMedSize);
        RadioGroup.add(radioMaxSize);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonExit);

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
            @Override
            public void stateChanged(ChangeEvent e) {
                e.getSource();
                if(!sliderBoardSize.getValueIsAdjusting()) {
                    int boardSize = sliderBoardSize.getValue();
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
    private void onMinSize() {
        setup.frameSize = GameSetup.FrameSize.MIN_SIZE;

    }

    private void onMedSize() {
        setup.frameSize = GameSetup.FrameSize.MED_SIZE;

    }

    private void onMaxSize() {
        setup.frameSize = GameSetup.FrameSize.MAX_SIZE;

    }

    private void onExit() {
        // add your code here if necessary


        dispose();
    }
}
