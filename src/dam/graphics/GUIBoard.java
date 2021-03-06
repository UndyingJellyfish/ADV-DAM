package dam.graphics;


import dam.control.ButtonListener;
import dam.logic.GameBoard;
import dam.menus.GameSetup;

import javax.swing.*;
import java.awt.*;


/**
 * Created by smous on 02-01-2017.
 * Member primarily responsible for file: Søren Mousten
 */
public class GUIBoard extends JPanel {


    // fields
    final private int boardSize;    // number of fields on one dimension
    final private GUIButton[][] arrayGUIButton; // button array of all buttons on board
    private final GameBoard gameBoard; // gameBoard board for game board

    // list of field types
    public enum FieldType {
        // indicates ownership of a particular button
        // binds enum values to a string, which is the relative file path of sound files

        EMPTY("Textures/Empty.png"),
        PLAYER0("Textures/CheckerBlack.png"),
        PLAYER1("Textures/CheckerWhite.png"),
        PLAYER0_KING("Textures/CheckerBlackKing.png"),
        PLAYER1_KING("Textures/CheckerWhiteKing.png");

        final private String path;

        FieldType(String path) {
            this.path = path;
        }

        public String getPath() {
            // returns the associated file path
            return this.path;
        }
    }

    // constructor
    public GUIBoard(GameBoard board, GameSetup setup) {
        // new boardSize*boardSize gridlayout using number of board squares from game setup
        super(new GridLayout(setup.boardSize, setup.boardSize));
        this.boardSize = setup.boardSize;

        this.gameBoard = board;
        // sets preferred size of game board to 90% of screen resolution
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int maxWidth = gd.getDisplayMode().getWidth();
        int maxHeight = gd.getDisplayMode().getHeight();


        double relativeSize;
        // switch to set relative size of frame to different cases
        switch (setup.frameSize) {
            case MIN_SIZE:
                relativeSize = 0.3;
                break;
            case MED_SIZE:
                relativeSize = 0.6;
                break;
            default:
            case MAX_SIZE:
                relativeSize = 0.9;
                break;

        }

        int ScreenSize = (int) Math.floor((maxHeight < maxWidth ? maxHeight : maxWidth) * relativeSize);
        // this pickes the smaller of the two screen dimensions and finds the floor of product with the relative size
        // this ensures that the screen as a maximum can take up 90 % of the samllest screen dimension
        // meaning that the entire window should be viewable when opened at the center of the screen

        this.setPreferredSize(new Dimension(ScreenSize, ScreenSize));

        // button array is 2d of length boardSize and boardSize
        arrayGUIButton = new GUIButton[boardSize][boardSize];

        // fills board with pieces of both players and empty fields. Uses gameBoard board to place pieces at right locations
        fillBoard(board);
    }

    // methods for returning fields

    public GUIButton[][] getArrayGUIButton() {
        return this.arrayGUIButton;
    }


    // methods of manipulating properties of the instance.
    public void fillInAllActionHandlers() {
        // adds actionListener to each field on board
        for (int yn = 0; yn < boardSize; yn++) {
            for (int xn = 0; xn < boardSize; xn++) {
                if((xn + yn) % 2 == 0){ // no reason to add actionListeners to disabled buttons
                    arrayGUIButton[xn][yn].addActionListener(new ButtonListener(arrayGUIButton[xn][yn], gameBoard, this));
                }

            }
        }
    }

    private void fillBoard(GameBoard boardToPopulate) {
        // fills the board with GUIButtons and field types according to board gameBoard
        for (int yn = 0; yn < boardSize; yn++) {
            for (int xn = 0; xn < boardSize; xn++) {
                try {
                    if (boardToPopulate.getPiecePlacement()[xn][yn].getPlayer().getIdentifier() == 0) {
                        arrayGUIButton[xn][yn] = new GUIButton(FieldType.PLAYER0, new Point(xn, yn));
                    } else if (boardToPopulate.getPiecePlacement()[xn][yn].getPlayer().getIdentifier() == 1) {
                        arrayGUIButton[xn][yn] = new GUIButton(FieldType.PLAYER1, new Point(xn, yn));
                    } else {
                        arrayGUIButton[xn][yn] = new GUIButton(FieldType.EMPTY, new Point(xn, yn));
                    }
                    this.add(arrayGUIButton[xn][yn]);
                } catch (NullPointerException e) {
                    arrayGUIButton[xn][yn] = new GUIButton(FieldType.EMPTY, new Point(xn, yn));
                }
            }
        }

    }

    // this had some problems, but would more effective at updating graphics than current solution
    // would save a bunch of code in ButtonListener

    public void updateGraphics(int fromX, int fromY, int toX, int toY, boolean setSuper) {
        if ((Math.abs(toX - fromX) > 1 && Math.abs(toY - fromY) > 1)){
            // happens only in case of jump moves
            int dirX = fromX > toX ? -1 : 1;
            int dirY = fromY > toY ? -1 : 1;
            arrayGUIButton[fromX + dirX][fromY + dirY].setFieldType(GUIBoard.FieldType.EMPTY);
        }
        // stores what kind of graphic was where moving from
        GUIBoard.FieldType fromField = arrayGUIButton[fromX][fromY].getFieldType();

        if (setSuper){
            // if piece is supposed to be super, set it to according super piece
            arrayGUIButton[toX][toY].setFieldType((fromField == FieldType.PLAYER0 || fromField == FieldType.PLAYER0_KING) ? FieldType.PLAYER0_KING : FieldType.PLAYER1_KING);
        } else {
            // else keep normal status
            arrayGUIButton[toX][toY].setFieldType(fromField);
        }
        arrayGUIButton[toX][toY].drawField(fromField);
        arrayGUIButton[fromX][fromY].drawField(FieldType.EMPTY);
    }





    // draws each field on board using field type
    public void paintBoard() {
        for (int yn = 0; yn < boardSize; yn++) {
            for (int xn = 0; xn < boardSize; xn++) {
                arrayGUIButton[xn][yn].drawField(arrayGUIButton[xn][yn].getFieldType());
            }
        }
    }

}