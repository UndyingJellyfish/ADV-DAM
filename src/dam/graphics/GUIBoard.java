package dam.graphics;


import dam.Control.ButtonListener;
import dam.abstractions.LogicBoard;

import javax.swing.*;
import java.awt.*;

/**
 * Created by smous on 02-01-2017.
 */
public class GUIBoard extends JPanel {

    // fields
    protected static int N; // number of fields on one dimension
    static int SIZE = 75;// pixel width and height of a field
    private GUIButton[][] buttonArray; // button array of all buttons on board
    private LogicBoard Logic; // logic board for game board

    // list of field types
    public enum FieldType { // indicates ownership of a particular button
        EMPTY,
        PLAYER0,
        PLAYER1,
        PLAYER0_KING,
        PLAYER1_KING
    }

    // constructor
    public GUIBoard(int N, LogicBoard board) {

        // new N*N gridlayout
        super(new GridLayout(N, N));
        this.N = N;
        Logic = board;
        // sets preferred size of game board
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int maxWidth = gd.getDisplayMode().getWidth();
        int maxHeight = gd.getDisplayMode().getHeight();

        int maxSize = (int) Math.floor((maxHeight < maxWidth ? maxHeight : maxWidth) * 0.9);

        this.setPreferredSize(new Dimension(maxSize, maxSize));

        // button array is 2d of length N and N
        buttonArray = new GUIButton[N][N];
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


    // methods of manipulating properties of the instance
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

    public void paintBoard() {
        for (int yn = 0; yn < N; yn++) {
            for (int xn = 0; xn < N; xn++) {
                buttonArray[xn][yn].drawField(buttonArray[xn][yn].getFieldType());
            }
        }
    }
}