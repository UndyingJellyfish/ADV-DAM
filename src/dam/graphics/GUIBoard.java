package dam.graphics;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import dam.Control.ButtonListener;
import dam.abstractions.*;

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
        PLAYER1,
        PLAYER2,
        PLAYER1DOUBLE,
        PLAYER2DOUBLE
    }

    // constructor
    public GUIBoard(int N, LogicBoard board) {

        // new N*N gridlayout
        super(new GridLayout(N, N));
        this.N = N;
        Logic = board;
        // size of board is (N*SIZE)*(N*SIZE)
        this.setPreferredSize(new Dimension(N*SIZE, N*SIZE));

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
                        if (boardToPopulate.getBrikPlacering()[xn][yn].getOwner().getIdentifier() == 0) {
                            buttonArray[xn][yn] = new GUIButton(N, FieldType.PLAYER1, new Point(xn, yn));

                        } else if (boardToPopulate.getBrikPlacering()[xn][yn].getOwner().getIdentifier() == 1) {
                            buttonArray[xn][yn] = new GUIButton(N, FieldType.PLAYER2, new Point(xn, yn));
                        } else {
                            buttonArray[xn][yn] = new GUIButton(N, FieldType.EMPTY, new Point(xn, yn));
                        }
                        this.add(buttonArray[xn][yn]);
                        // this.buttonArray[i][j].setActionListener();
                        //System.out.println("New button at: [" + buttonArray[xn][yn].getLocation().getX() + ", " + buttonArray[xn][yn].getLocation().getY() + "], identifier: " + this.buttonArray[xn][yn].getAssociatedPiece().getIdentifier());

                    } catch (NullPointerException e) {
                        buttonArray[xn][yn] = new GUIButton(N, FieldType.EMPTY, new Point(xn, yn));
                    }
                }
            }
        }
    }
}