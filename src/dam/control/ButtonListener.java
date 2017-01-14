package dam.control;

import dam.abstractions.LogicBoard;
import dam.graphics.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static dam.graphics.GUIBoard.FieldType.*;

/**
 * Created by Nicklas on 05-01-2017.
 * Member primarily responsible for file: Nicklas Juul
 */


public class ButtonListener implements ActionListener {
    // fields are final because they never change
    final private GUIButton RelevantButton;
    final private LogicBoard Board;
    final private GUIBoard graphics;

    // constructors
    public ButtonListener(GUIButton button, LogicBoard board, GUIBoard graphics) {
        this.RelevantButton = button;
        this.Board = board;
        this.graphics = graphics;
    }

    // action on click
    public void actionPerformed(ActionEvent e) {
        try {
            // debug code
            /*
            // prints position of GUIButton clicked
            System.out.println("Field pressed X: " + RelevantButton.getPosition().getX() + ", Y: " + RelevantButton.getPosition().getY());
            // prints position of last click if it exists
            if (Board.hasLastClicked()) {
                System.out.println("Last pressed X: " + Board.getLastClickedGUIButton().getPosition().getX()
                        + ", Y: " + Board.getLastClickedGUIButton().getPosition().getY());
            }
            */
            if (RelevantButton != null) {
                // TODO: this is god awful and should be refactored immediately

                // continue if lastClicked exists, else set as lastClick
                if (Board.hasLastClicked()) {

                    // uses isLegalMove method from logic to check if the move from lastClicked to button just clicked is legal
                    if (Board.isLegalMove((int) Board.getLastClickedGUIButton().getPosition().getX(),
                            (int) Board.getLastClickedGUIButton().getPosition().getY(),
                            (int) RelevantButton.getPosition().getX(),
                            (int) RelevantButton.getPosition().getY())) {

                        // sets button just licked as NewClickedGUIButton
                        Board.setNewClickedGUIButton(RelevantButton);

                        // set newClicked to same field type as lastClicked
                        Board.getNewClickedGUIButton().setFieldType(Board.getLastClickedGUIButton().getFieldType());

                        // set lastClicked to empty field type
                        Board.getLastClickedGUIButton().setFieldType(EMPTY);

                        // uses move method from logic with lastClicked position and newClicked position as arguments
                        Board.Move((int) Board.getLastClickedGUIButton().getPosition().getX(),
                                (int) Board.getLastClickedGUIButton().getPosition().getY(),
                                (int) Board.getNewClickedGUIButton().getPosition().getX(),
                                (int) Board.getNewClickedGUIButton().getPosition().getY());



                        // checks piece jumps over another piece
                        if ((Math.abs(Board.getNewClickedGUIButton().getPosition().getX() - Board.getLastClickedGUIButton().getPosition().getX()) > 1 &&
                                Math.abs(Board.getNewClickedGUIButton().getPosition().getY() - Board.getLastClickedGUIButton().getPosition().getY()) > 1)) {

                            // dirX and dirY are the direction of jump
                            int dirX = (int) Board.getLastClickedGUIButton().getPosition().getX() > (int) Board.getNewClickedGUIButton().getPosition().getX() ? -1 : 1;
                            int dirY = (int) Board.getLastClickedGUIButton().getPosition().getY() > (int) Board.getNewClickedGUIButton().getPosition().getY() ? -1 : 1;

                            // sets the field type of piece jumped over to empty
                            graphics.getButtonArray()[(int) Board.getLastClickedGUIButton().getPosition().getX() + dirX]
                                    [(int) Board.getLastClickedGUIButton().getPosition().getY() + dirY].setFieldType(GUIBoard.FieldType.EMPTY);

                        }

                        // sets the field type to king status if piece reaches opposite end
                        if ((Board.getNewClickedGUIButton().getPosition().getY() == Board.getNewClickedGUIButton().getN() - 1) && (Board.getNewClickedGUIButton().getFieldType() == PLAYER0))
                            Board.getNewClickedGUIButton().setFieldType(PLAYER0_KING);
                        else if ((Board.getNewClickedGUIButton().getPosition().getY()) == 0 && (Board.getNewClickedGUIButton().getFieldType() == PLAYER1))
                            Board.getNewClickedGUIButton().setFieldType(PLAYER1_KING);

                        // draws the lastClicked and newClicked according to field type
                        Board.getNewClickedGUIButton().drawField(Board.getNewClickedGUIButton().getFieldType());
                        Board.getLastClickedGUIButton().drawField(Board.getLastClickedGUIButton().getFieldType());

                        // plays sound for movement (aka. satisfying wooden *click*)
                        new AudioPlayer(AudioPlayer.AUDIO.MOVE);

                        // calls the endTurn method from logic, which changes turn of players
                        Board.endTurn();

                        // resets lastClicked
                        Board.setLastClickedGUIButton(null);
                        Board.setNewClickedGUIButton(null);


                    } else {
                        System.out.println("Move is not legal");
                        // move is not legal and resets lastClicked
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                        Board.setLastClickedGUIButton(null);
                    }
                } else {
                    System.out.println("No lastClicked button exists");
                    // lastClicked can only be a piece of current player
                    if (Board.getPieceAtPosition(RelevantButton.getPosition()).getOwner() != Board.getCurrentPlayer()) {
                        System.out.println("Not your turn!");
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                    }
                    // empty fields can not be set as lastClicked
                    else if ((RelevantButton.getFieldType() != PLAYER0 && RelevantButton.getFieldType() != PLAYER0_KING)
                            && (RelevantButton.getFieldType() != PLAYER1 && RelevantButton.getFieldType() != PLAYER1_KING))
                        System.out.println("Ignoring click as first click");
                    else
                        Board.setLastClickedGUIButton(this.RelevantButton);
                }

            }


        } catch (NullPointerException ex) {
            System.out.println("Clicking returned: " + ex.getClass().getName());
        }

    }
}
