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
    final private GUIButton relevantButton;
    final private GameBoard board;
    final private GUIBoard graphics;
    private int boardSize;

    // constructors
    public ButtonListener(GUIButton button, GameBoard board, GUIBoard graphics) {
        this.relevantButton = button;
        this.board = board;
        this.graphics = graphics;
        this.boardSize = this.board.getBoardSize(); // no reason to make a parameter when board size is supposedly constant
    }

    // action on click
    public void actionPerformed(ActionEvent e) {
        try {
            if (relevantButton != null) {
                // continue if lastClicked exists, else set as lastClick
                if (board.hasLastClicked()) {

                    // variables introduced because, else this section of code is close to unreadable and unmaintainable
                    int fromX = (int) board.getLastClickedGUIButton().getPosition().getX();
                    int fromY = (int) board.getLastClickedGUIButton().getPosition().getY();
                    GUIBoard.FieldType fromField = board.getLastClickedGUIButton().getFieldType();
                    int thisX = (int) relevantButton.getPosition().getX();
                    int thisY = (int) relevantButton.getPosition().getY();


                    // checks whether move is legal
                    if (board.isLegalMove(fromX, fromY, thisX, thisY)) {

                        // sets button just clicked as NewClickedGUIButton
                        board.setNewClickedGUIButton(relevantButton);
                        int toX = (int) board.getNewClickedGUIButton().getPosition().getX();
                        int toY = (int) board.getNewClickedGUIButton().getPosition().getY();

                        // set newClicked to same field type as lastClicked
                        board.getNewClickedGUIButton().setFieldType(fromField);



                        // uses move method from logic with lastClicked position and newClicked position as arguments
                        boolean moveFromSuper = board.getPiecePlacement()[fromX][fromY].isSuperPiece();
                        boolean moveToSuperPlayer0 = ( (toY == boardSize - 1) && (fromField == PLAYER0) );
                        boolean moveToSuperPlayer1 = ( (toY == 0) && (fromField == PLAYER1) );

                        board.move(fromX, fromY, toX, toY);

                        // checks piece jumps over another piece
                        if ((Math.abs(toX - fromX) > 1 && Math.abs(toY - fromY) > 1)) {

                            // dirX and dirY are the direction of jump
                            int dirX = fromX > toX ? -1 : 1;
                            int dirY = fromY > toY ? -1 : 1;

                            // sets the field type of piece jumped over to empty
                            graphics.getArrayGUIButton()[fromX + dirX][fromY + dirY].setFieldType(GUIBoard.FieldType.EMPTY);
                        }

                        // sets the field type to king status if piece reaches opposite end or is already king

                        if (moveFromSuper && fromField == PLAYER0){
                            board.getNewClickedGUIButton().setFieldType(PLAYER0_KING);
                        } else if (moveFromSuper && fromField == PLAYER1){
                            board.getNewClickedGUIButton().setFieldType(PLAYER1_KING);
                        } else if ( (toY == boardSize - 1) && (fromField == PLAYER0) ){
                            board.getNewClickedGUIButton().setFieldType(PLAYER0_KING);
                        } else if ( toY == 0 && (fromField == PLAYER1)){
                            board.getNewClickedGUIButton().setFieldType(PLAYER1_KING);
                        }

                        // set lastClicked to empty field type
                        board.getLastClickedGUIButton().setFieldType(EMPTY);

                        boolean shouldBeSuperPiece = moveFromSuper || moveToSuperPlayer0 || moveToSuperPlayer1;

                        board.getPiecePlacement()[toX][toY].setSuperPiece(shouldBeSuperPiece);

                        // draws the lastClicked and newClicked according to field type
                        board.getNewClickedGUIButton().drawField(board.getNewClickedGUIButton().getFieldType());
                        board.getLastClickedGUIButton().drawField(board.getLastClickedGUIButton().getFieldType());



                        // plays sound for movement (aka. satisfying wooden *click*)
                        new AudioPlayer(AudioPlayer.AUDIO.MOVE);

                        // calls the endTurn method from logic, which changes turn of players
                        board.endTurn();

                        // resets lastClicked and newClicked
                        board.setLastClickedGUIButton(null);
                        board.setNewClickedGUIButton(null);


                    } else {
                        // move is not legal and resets lastClicked
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                        board.setLastClickedGUIButton(null);
                    }
                } else {
                    // lastClicked can only be a piece of current player
                    if (board.getPieceAtPosition(relevantButton.getPosition()).getPlayer() != board.getCurrentPlayer()) {
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                    }
                    else
                        board.setLastClickedGUIButton(this.relevantButton);
                }

            }


        } catch (NullPointerException npex) {
            System.out.println("Clicking returned NullPointerException, the develeoper should be ashamed of himself");
        } catch (Exception ex){
            System.out.println("Unspecified error occurred");
        }


    }
}
