package dam.control;

import dam.logic.GameBoard;
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
    final private GameBoard Board;
    final private GUIBoard Graphics;
    private int BoardSize;

    // constructors
    public ButtonListener(GUIButton button, GameBoard board, GUIBoard graphics) {
        this.RelevantButton = button;
        this.Board = board;
        this.Graphics = graphics;
        this.BoardSize = Board.getBoardSize(); // no reason to make a parameter when board size is supposedly constant
    }

    // action on click
    public void actionPerformed(ActionEvent e) {
        try {
            if (RelevantButton != null) {
                // continue if lastClicked exists, else set as lastClick
                if (Board.hasLastClicked()) {

                    // variables introduced because, else this section of code is close to unreadable and unmaintainable
                    int fromX = (int) Board.getLastClickedGUIButton().getPosition().getX();
                    int fromY = (int) Board.getLastClickedGUIButton().getPosition().getY();
                    GUIBoard.FieldType fromField = Board.getLastClickedGUIButton().getFieldType();
                    int thisX = (int) RelevantButton.getPosition().getX();
                    int thisY = (int) RelevantButton.getPosition().getY();


                    // checks whether move is legal
                    if (Board.isLegalMove(fromX, fromY, thisX, thisY)) {

                        // sets button just clicked as NewClickedGUIButton
                        Board.setNewClickedGUIButton(RelevantButton);
                        int toX = (int) Board.getNewClickedGUIButton().getPosition().getX();
                        int toY = (int) Board.getNewClickedGUIButton().getPosition().getY();

                        // set newClicked to same field type as lastClicked
                        Board.getNewClickedGUIButton().setFieldType(fromField);



                        // uses move method from logic with lastClicked position and newClicked position as arguments
                        boolean moveFromSuper = Board.getPiecePlacement()[fromX][fromY].isSuperPiece();
                        boolean moveToSuperPlayer0 = ( (toY == BoardSize - 1) && (fromField == PLAYER0) );
                        boolean moveToSuperPlayer1 = ( (toY == 0) && (fromField == PLAYER1) );

                        Board.Move(fromX, fromY, toX, toY);

                        // checks piece jumps over another piece
                        if ((Math.abs(toX - fromX) > 1 && Math.abs(toY - fromY) > 1)) {

                            // dirX and dirY are the direction of jump
                            int dirX = fromX > toX ? -1 : 1;
                            int dirY = fromY > toY ? -1 : 1;

                            // sets the field type of piece jumped over to empty
                            Graphics.getGUIButtonArray()[fromX + dirX][fromY + dirY].setFieldType(GUIBoard.FieldType.EMPTY);
                        }

                        // sets the field type to king status if piece reaches opposite end or is already king

                        if (moveFromSuper && fromField == PLAYER0){
                            Board.getNewClickedGUIButton().setFieldType(PLAYER0_KING);
                        } else if (moveFromSuper && fromField == PLAYER1){
                            Board.getNewClickedGUIButton().setFieldType(PLAYER1_KING);
                        } else if ( (toY == BoardSize - 1) && (fromField == PLAYER0) ){
                            Board.getNewClickedGUIButton().setFieldType(PLAYER0_KING);
                        } else if ( toY == 0 && (fromField == PLAYER1)){
                            Board.getNewClickedGUIButton().setFieldType(PLAYER1_KING);
                        }

                        // set lastClicked to empty field type
                        Board.getLastClickedGUIButton().setFieldType(EMPTY);

                        boolean shouldBeSuperPiece = moveFromSuper || moveToSuperPlayer0 || moveToSuperPlayer1;

                        Board.getPiecePlacement()[toX][toY].setSuperPiece(shouldBeSuperPiece);

                        // draws the lastClicked and newClicked according to field type
                        Board.getNewClickedGUIButton().drawField(Board.getNewClickedGUIButton().getFieldType());
                        Board.getLastClickedGUIButton().drawField(Board.getLastClickedGUIButton().getFieldType());



                        // plays sound for movement (aka. satisfying wooden *click*)
                        new AudioPlayer(AudioPlayer.AUDIO.MOVE);

                        // calls the endTurn method from logic, which changes turn of players
                        Board.endTurn();

                        // resets lastClicked and newClicked
                        Board.setLastClickedGUIButton(null);
                        Board.setNewClickedGUIButton(null);


                    } else {
                        // move is not legal and resets lastClicked
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                        Board.setLastClickedGUIButton(null);
                    }
                } else {
                    // lastClicked can only be a piece of current player
                    if (Board.getPieceAtPosition(RelevantButton.getPosition()).getOwner() != Board.getCurrentPlayer()) {
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                    }
                    else
                        Board.setLastClickedGUIButton(this.RelevantButton);
                }

            }


        } catch (NullPointerException npex) {
            System.out.println("Clicking returned NullPointerException, the develeoper should be ashamed of himself");
        } catch (Exception ex){
            System.out.println("Unspecified error occurred");
        }


    }
}
