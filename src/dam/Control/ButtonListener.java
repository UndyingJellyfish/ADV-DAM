package dam.Control;

import dam.abstractions.*;
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
    private Point ButtonLocation;
    private GUIBoard graphics;

    // constructors
    public ButtonListener(GUIButton button, LogicBoard board, GUIBoard graphics) {
        this.RelevantButton = button;
        this.Board = board;
        this.graphics = graphics;

        this.ButtonLocation = new Point(RelevantButton.getPosition());

    }

    private boolean locationIsInBounds() {
        return (RelevantButton.getLastClicked().getX() > -1 && RelevantButton.getLastClicked().getY() > -1 &&
                RelevantButton.getLastClicked().getX() < RelevantButton.getN() &&
                RelevantButton.getLastClicked().getY() < RelevantButton.getN());
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

                    if (Board.isLegalMove( (int) Board.getLastClickedGUIButton().getPosition().getX(),
                            (int) Board.getLastClickedGUIButton().getPosition().getY(),
                            (int) RelevantButton.getPosition().getX(),
                            (int) RelevantButton.getPosition().getY() ) ) {
                        Board.setNewClickedGUIButton(RelevantButton);


                        Board.getNewClickedGUIButton().setFieldType(Board.getLastClickedGUIButton().getFieldType());
                        Board.getLastClickedGUIButton().setFieldType(EMPTY);


                        //Board.getLastClickedGUIButton().setLocation(Board.getNewClickedGUIButton().getLocation());
                        //Board.getNewClickedGUIButton().setLocation(temp.getLocation());


                        Board.Move((int) Board.getLastClickedGUIButton().getPosition().getX(),
                                (int) Board.getLastClickedGUIButton().getPosition().getY(),
                                (int) Board.getNewClickedGUIButton().getPosition().getX(),
                                (int) Board.getNewClickedGUIButton().getPosition().getY());

                        if ((Math.abs(Board.getNewClickedGUIButton().getPosition().getX() - Board.getLastClickedGUIButton().getPosition().getX()) > 1 &&
                                Math.abs(Board.getNewClickedGUIButton().getPosition().getY() - Board.getLastClickedGUIButton().getPosition().getY()) > 1)){
                            int dirX = (int) Board.getLastClickedGUIButton().getPosition().getX() > (int) Board.getNewClickedGUIButton().getPosition().getX() ? -1 : 1;
                            int dirY = (int) Board.getLastClickedGUIButton().getPosition().getY() > (int) Board.getNewClickedGUIButton().getPosition().getY() ? -1 : 1;
                            graphics.getButtonArray()[(int)Board.getLastClickedGUIButton().getPosition().getX() + dirX][(int)Board.getLastClickedGUIButton().getPosition().getY() + dirY].setFieldType(GUIBoard.FieldType.EMPTY);
                            graphics.getButtonArray()[(int)Board.getLastClickedGUIButton().getPosition().getX() + dirX][(int)Board.getLastClickedGUIButton().getPosition().getY() + dirY].drawField(GUIBoard.FieldType.EMPTY);
                        }

                        if (Board.getNewClickedGUIButton().getPosition().getY() == Board.getNewClickedGUIButton().getN()-1){
                            Board.getNewClickedGUIButton().setFieldType(PLAYER0_KING);
                        }
                        else if(Board.getNewClickedGUIButton().getPosition().getY() == 0) {
                            Board.getNewClickedGUIButton().setFieldType(PLAYER1_KING);
                        }

                        Board.getNewClickedGUIButton().drawField(Board.getNewClickedGUIButton().getFieldType());
                        Board.getLastClickedGUIButton().drawField(Board.getLastClickedGUIButton().getFieldType());



                        Board.printBoard();

                        Board.endTurn();



                        Board.setLastClickedGUIButton(null);


                    }
                    else {
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

    /*
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Point pFrom;
            Point pTo;


            System.out.println("Field pressed X: " + RelevantButton.getPosition().getX()
                    + ", Y: " + RelevantButton.getPosition().getY());
            if (Board.hasLastClicked()) {
                pFrom = new Point(Board.getLastClickedGUIButton().getPosition());
                System.out.println("Last pressed X: " + pFrom.getX()
                        + ", Y: " + pFrom.getY());
            }

            if (RelevantButton instanceof GUIButton) {
                if (Board.hasLastClicked()) {
                    Board.setNewClickedGUIButton(RelevantButton);
                    pFrom = new Point(Board.getLastClickedGUIButton().getPosition());
                    pTo = new Point(Board.getNewClickedGUIButton().getPosition());

                    GUIBoard.FieldType fromField = Board.getLastClickedGUIButton().getFieldType();
                    GUIBoard.FieldType toField = Board.getNewClickedGUIButton().getFieldType();

                    // System.out.println("(Debug) Relevant button: " + this.RelevantButton.getPosition() + " pTo: " + pTo.toString()); // debug message

                    if (Board.isLegalMove((int) pFrom.getX(), (int) pFrom.getY(), (int) ButtonLocation.getX(), (int) ButtonLocation.getY())) {

                        // draws the new fields in the appropriate colors
                        Board.getNewClickedGUIButton().drawField(toField);
                        Board.getLastClickedGUIButton().drawField(fromField);



                        // moves the relevant pieces on the LogicBoard instance
                        Board.Move((int) pFrom.getX(), (int) pFrom.getY(), (int) pTo.getX(), (int) pTo.getY());

                        // changes the field types to reflect changes in the LogicBoard
                        Board.getNewClickedGUIButton().setFieldType(fromField);
                        Board.getLastClickedGUIButton().setFieldType(EMPTY);


                        // debugging messages to ensure correct moves
                        Board.printBoard();
                        System.out.println(Board.getBrikPlacering()[(int)pTo.getX()][(int)pTo.getY()].getOwner().getPlayerName() + " moved piece from [" + pFrom.getX() + ", " + pFrom.getY() + "] to [" + pTo.getX() + ", " + pTo.getY() + "]");


                        // ends the current players turn
                        Board.endTurn();

                        // resets the last clicked button
                        Board.setLastClickedGUIButton(null);
                        Board.setNewClickedGUIButton(null);


                    } else {
                        System.out.println("Move is not legal");
                        Board.setLastClickedGUIButton(null);
                    }
                } else {
                    System.out.println("No lastClicked button exists");
                    if (Board.getPieceAtPosition(RelevantButton.getPosition()).getOwner() != Board.getCurrentPlayer())
                        System.out.println("Not your turn!");
                    else if (RelevantButton.getFieldType() != PLAYER0 && RelevantButton.getFieldType() != PLAYER1)
                        System.out.println("Ignoring click as first click");
                    else
                        Board.setLastClickedGUIButton(this.RelevantButton);
                }
                return;

            }


        } catch (NullPointerException ex) {
            System.out.println("Clicking returned: " + ex.getClass().getName());
        }
    }*/



}
