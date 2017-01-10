package dam.Control;

import dam.abstractions.LogicBoard;
import dam.graphics.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static dam.graphics.GUIBoard.FieldType.*;

/**
 * Created by Nicklas on 05-01-2017.
 */


public class ButtonListener implements ActionListener {
    // fields
    private GUIButton RelevantButton;
    private LogicBoard Board;
    private GUIBoard graphics;

    // constructors
    public ButtonListener(GUIButton button, LogicBoard board, GUIBoard graphics) {
        this.RelevantButton = button;
        this.Board = board;
        this.graphics = graphics;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println("Field pressed X: " + RelevantButton.getPosition().getX()
                    + ", Y: " + RelevantButton.getPosition().getY());
            if (Board.hasLastClicked()) {
                System.out.println("Last pressed X: " + Board.getLastClickedGUIButton().getPosition().getX()
                        + ", Y: " + Board.getLastClickedGUIButton().getPosition().getY());
            }

            if (RelevantButton instanceof GUIButton) {
                if (Board.hasLastClicked()) {
                    if (Board.isLegalMove((int) Board.getLastClickedGUIButton().getPosition().getX(),
                            (int) Board.getLastClickedGUIButton().getPosition().getY(),
                            (int) RelevantButton.getPosition().getX(),
                            (int) RelevantButton.getPosition().getY())) {
                        Board.setNewClickedGUIButton(RelevantButton);

                        Board.getNewClickedGUIButton().setFieldType(Board.getLastClickedGUIButton().getFieldType());
                        Board.getLastClickedGUIButton().setFieldType(EMPTY);

                        Board.Move((int) Board.getLastClickedGUIButton().getPosition().getX(),
                                (int) Board.getLastClickedGUIButton().getPosition().getY(),
                                (int) Board.getNewClickedGUIButton().getPosition().getX(),
                                (int) Board.getNewClickedGUIButton().getPosition().getY());

                        if ((Math.abs(Board.getNewClickedGUIButton().getPosition().getX() - Board.getLastClickedGUIButton().getPosition().getX()) > 1 &&
                                Math.abs(Board.getNewClickedGUIButton().getPosition().getY() - Board.getLastClickedGUIButton().getPosition().getY()) > 1)) {
                            int dirX = (int) Board.getLastClickedGUIButton().getPosition().getX() > (int) Board.getNewClickedGUIButton().getPosition().getX() ? -1 : 1;
                            int dirY = (int) Board.getLastClickedGUIButton().getPosition().getY() > (int) Board.getNewClickedGUIButton().getPosition().getY() ? -1 : 1;
                            graphics.getButtonArray()[(int) Board.getLastClickedGUIButton().getPosition().getX() + dirX][(int) Board.getLastClickedGUIButton().getPosition().getY() + dirY].setFieldType(GUIBoard.FieldType.EMPTY);
                            graphics.getButtonArray()[(int) Board.getLastClickedGUIButton().getPosition().getX() + dirX][(int) Board.getLastClickedGUIButton().getPosition().getY() + dirY].drawField(GUIBoard.FieldType.EMPTY);
                        }

                        if (Board.getNewClickedGUIButton().getPosition().getY() == Board.getNewClickedGUIButton().getN() - 1) {
                            Board.getNewClickedGUIButton().setFieldType(PLAYER0_KING);
                        } else if (Board.getNewClickedGUIButton().getPosition().getY() == 0) {
                            Board.getNewClickedGUIButton().setFieldType(PLAYER1_KING);
                        }

                        Board.getNewClickedGUIButton().drawField(Board.getNewClickedGUIButton().getFieldType());
                        Board.getLastClickedGUIButton().drawField(Board.getLastClickedGUIButton().getFieldType());

                        // Board.printBoard(); //debugging, used to make sure pieces moves properly on logic board
                        Board.endTurn();
                        Board.setLastClickedGUIButton(null);


                    } else {
                        System.out.println("Move is not legal");
                        Board.setLastClickedGUIButton(null);
                    }
                } else {
                    System.out.println("No lastClicked button exists");
                    if (Board.getPieceAtPosition(RelevantButton.getPosition()).getOwner() != Board.getCurrentPlayer())
                        System.out.println("Not your turn!");
                    else if ((RelevantButton.getFieldType() != PLAYER0 && RelevantButton.getFieldType() != PLAYER0_KING)
                            && (RelevantButton.getFieldType() != PLAYER1 && RelevantButton.getFieldType() != PLAYER1_KING))
                        System.out.println("Ignoring click as first click");
                    else
                        Board.setLastClickedGUIButton(this.RelevantButton);
                }
                return;

            }


        } catch (NullPointerException ex) {
            System.out.println("Clicking returned: " + ex.getClass().getName());
        }

    }
}
