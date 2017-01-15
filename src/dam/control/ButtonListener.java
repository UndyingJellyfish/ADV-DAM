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

    // fields are final because they never change reference, though their internal values do change
    final private GUIButton relevantButton;
    final private GameBoard gameBoard;
    final private GUIBoard graphics;
    final private int boardSize;

    // constructors
    public ButtonListener(GUIButton button, GameBoard gameBoard, GUIBoard graphics) {
        this.relevantButton = button;
        this.gameBoard = gameBoard;
        this.graphics = graphics;
        this.boardSize = this.gameBoard.getBoardSize(); // no reason to make a parameter when gameBoard size is supposedly constant
    }

    // action on click
    public void actionPerformed(ActionEvent e) {
        try {
            if (relevantButton != null) {
                // continue if lastClicked exists, else set as lastClick
                if (gameBoard.hasLastClicked()) {

                    // variables introduced because, else this section of code is close to unreadable and unmaintainable
                    int fromX = (int) gameBoard.getLastClickedGUIButton().getPosition().getX();
                    int fromY = (int) gameBoard.getLastClickedGUIButton().getPosition().getY();
                    GUIBoard.FieldType fromField = gameBoard.getLastClickedGUIButton().getFieldType();
                    int toX = (int) relevantButton.getPosition().getX();
                    int toY = (int) relevantButton.getPosition().getY();


                    // checks whether move is legal
                    if (gameBoard.isLegalMove(fromX, fromY, toX, toY)) {


                        // set newClicked to same field type as lastClicked
                        relevantButton.setFieldType(fromField);

                        // uses move method from logic with lastClicked position and newClicked position as arguments
                        boolean moveFromSuper = gameBoard.getPiecePlacement()[fromX][fromY].isSuperPiece();
                        boolean moveToSuperPlayer0 = ( (toY == boardSize - 1) && (fromField == PLAYER0) );
                        boolean moveToSuperPlayer1 = ( (toY == 0) && (fromField == PLAYER1) );

                        gameBoard.move(fromX, fromY, toX, toY);

                        // checks piece jumps over another piece
                        if ((Math.abs(toX - fromX) > 1 && Math.abs(toY - fromY) > 1)) {

                            // dirX and dirY are the direction of jump
                            int dirX = fromX > toX ? -1 : 1;
                            int dirY = fromY > toY ? -1 : 1;

                            // sets the field type of piece jumped over to empty
                            graphics.getArrayGUIButton()[fromX + dirX][fromY + dirY].setFieldType(GUIBoard.FieldType.EMPTY);
                        }


                        // set lastClicked to empty field type
                        // which updates the graphics
                        gameBoard.getLastClickedGUIButton().setFieldType(EMPTY);

                        boolean shouldBeSuperPiece = moveFromSuper || moveToSuperPlayer0 || moveToSuperPlayer1;

                        if (shouldBeSuperPiece){
                            // updates logic to super piece
                            gameBoard.getPiecePlacement()[toX][toY].setSuperPiece(true);

                            // sets the field type to king status if piece reaches opposite end or is already king
                            GUIBoard.FieldType toField = relevantButton.getFieldType();
                            relevantButton.setFieldType((toField == PLAYER0 || toField == PLAYER0_KING) ? PLAYER0_KING : PLAYER1_KING);
                        } else {
                            // else the piece keeps its normal status
                            // social mobility is non-existent in the world of checkers
                            relevantButton.setFieldType(fromField);
                        }

                        // plays sound for movement (aka. satisfying wooden *click*)
                        new AudioPlayer(AudioPlayer.AUDIO.MOVE);

                        // calls the endTurn method from logic, which changes turn of players and checks win conditions
                        gameBoard.endTurn();

                        // resets lastClicked
                        gameBoard.setLastClickedGUIButton(null);


                    } else {
                        // move is not legal and resets lastClicked
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                        gameBoard.setLastClickedGUIButton(null);
                    }
                } else {
                    // lastClicked can only be a piece of current player
                    if (gameBoard.getPieceAtPosition(relevantButton.getPosition()).getPlayer() != gameBoard.getCurrentPlayer()) {
                        new AudioPlayer(AudioPlayer.AUDIO.ERROR);
                    }
                    else
                        gameBoard.setLastClickedGUIButton(this.relevantButton);
                }

            }


        } catch (NullPointerException npex) {
            System.out.println("Clicking returned NullPointerException, the develeoper should be ashamed of himself");
        } catch (Exception ex){
            System.out.println("Unspecified error occurred");
        }


    }
}
