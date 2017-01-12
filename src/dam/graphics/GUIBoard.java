package dam.graphics;


import dam.Control.ButtonListener;
import dam.abstractions.LogicBoard;
import dam.menus.GameSetup;

import javax.swing.*;
import java.awt.*;

/**
 * Created by smous on 02-01-2017.
 */
public class GUIBoard extends JPanel {

    // fields
    private int N;// int N; // number of fields on one dimension
    static int SIZE = 75;// pixel width and height of a field
    private GUIButton[][] buttonArray; // button array of all buttons on board
    private LogicBoard Logic; // logic board for game board

    // list of field types
    public enum FieldType { // indicates ownership of a particular button
        EMPTY("Resources\\Textures\\Empty.png"),
        PLAYER0("Resources\\Textures\\CheckerBlack.png"),
        PLAYER1("Resources\\Textures\\CheckerWhite.png"),
        PLAYER0_KING("Resources\\Textures\\CheckerBlackKing.png"),
        PLAYER1_KING("Resources\\Textures\\CheckerWhiteKing.png");

        private String text;

        FieldType(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static FieldType fromString(String text) {
            if (text != null) {
                for (FieldType f : FieldType.values()) {
                    if (text.equalsIgnoreCase(f.text)) {
                        return f;
                    }
                }
            }
            return null;
        }
    }

    // constructor
    public GUIBoard(LogicBoard board, GameSetup setup) {
        // new N*N gridlayout using number of board squares from game setup
        super(new GridLayout(setup.boardSquares, setup.boardSquares));
        //this.N = N;
        this.N = setup.boardSquares;

        Logic = board;
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

        this.setPreferredSize(new Dimension(ScreenSize, ScreenSize));

        // button array is 2d of length N and N
        buttonArray = new GUIButton[N][N];

        // fills board with pieces of both players and empty fields. Uses logic board to place pieces at right locations
        fillBoard(board);
    }

    // methods for returning fields
    public int getN() {
        return this.N;
    }

    public LogicBoard getLogicBoard() {
        return this.Logic;
    }

    public GUIButton[][] getButtonArray() {
        return this.buttonArray;
    }


    // methods of manipulating properties of the instance. Adds actionListener to each field on board
    public void fillInAllActionHandlers() {
        for (int yn = 0; yn < N; yn++) {
            for (int xn = 0; xn < N; xn++) {
                buttonArray[xn][yn].addActionListener(new ButtonListener(buttonArray[xn][yn], this.getLogicBoard(), this));
            }
        }
    }


    public void printInfoAtLocation(int x, int y) {
        GUIButton temp = this.buttonArray[x][y];
        Point p = temp.getPosition();
        System.out.println(p.getLocation());
    }

    public void addButtonAt(GUIButton buttonToAdd, int x, int y) {
        this.buttonArray[x][y].add(buttonToAdd);
    }

    // fills the board with GUIButtons and fieldtypes according til boardlogic.
    public void fillBoard(LogicBoard boardToPopulate) {
        if (boardToPopulate.countAllPieces() >= 0) {
            for (int yn = 0; yn < N; yn++) {
                for (int xn = 0; xn < N; xn++) {
                    try {
                        if (boardToPopulate.getPiecePlacement()[xn][yn].getOwner().getIdentifier() == 0) {
                            buttonArray[xn][yn] = new GUIButton(N, FieldType.PLAYER0, new Point(xn, yn));
                        } else if (boardToPopulate.getPiecePlacement()[xn][yn].getOwner().getIdentifier() == 1) {
                            buttonArray[xn][yn] = new GUIButton(N, FieldType.PLAYER1, new Point(xn, yn));
                        } else {
                            buttonArray[xn][yn] = new GUIButton(N, FieldType.EMPTY, new Point(xn, yn));
                        }
                        this.add(buttonArray[xn][yn]);
                    } catch (NullPointerException e) {
                        buttonArray[xn][yn] = new GUIButton(N, FieldType.EMPTY, new Point(xn, yn));
                    }
                }
            }
        }
    }

    // draws each field on board using field type
    public void paintBoard() {
        for (int yn = 0; yn < N; yn++) {
            for (int xn = 0; xn < N; xn++) {
                buttonArray[xn][yn].drawField(buttonArray[xn][yn].getFieldType());
            }
        }
    }
}