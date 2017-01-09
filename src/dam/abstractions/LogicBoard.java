package dam.abstractions;

import dam.graphics.GUIButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */

public class LogicBoard {
    // fields
    private CheckerPiece[][] brikPlacering;
    private CheckerPiece lastPieceMoved;
    private int boardSize;
    private GUIButton lastClickedGUIButton;
    private GUIButton newClickedGUIButton;
    private Player player1;
    private Player player2;

    private Player currentPlayer;


    // constructors
    public LogicBoard(CheckerPiece[][] brikker, int size) {
        this.brikPlacering = brikker;
        this.boardSize = size;
    }

    // methods for returning fields and other properties
    public void setLastClickedGUIButton(GUIButton button) {
        this.lastClickedGUIButton = button;
    }

    public GUIButton getLastClickedGUIButton() {
        return this.lastClickedGUIButton;
    }

    public void setNewClickedGUIButton(GUIButton button) {
        this.newClickedGUIButton = button;
    }

    public GUIButton getNewClickedGUIButton() {
        return this.newClickedGUIButton;
    }

    public boolean hasLastClicked() {
        return (lastClickedGUIButton != null);
    }

    public CheckerPiece[][] getBrikPlacering() {
        return this.brikPlacering;
    }

    public int countAllPieces() {
        int sum = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (brikPlacering[i][j] != null) sum++;
            }
        }
        return sum;
    }

    public int countPiecesForPlayer(Player targetPlayer) {
        int sum = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (brikPlacering[i][j] != null && (brikPlacering[i][j].getOwner().getIdentifier() == targetPlayer.getIdentifier()))
                    sum++;
            }
        }
        return sum;
    }

    // methods for returning properties of relationships
    public boolean locationIsEmpty(int x, int y) {
        return brikPlacering[x][y] == null;
    }

    public boolean isLegalMove(int fromX, int fromY, int toX, int toY) {
        // used for checking whether it's allowed to move from one location to another
        try {
            if (brikPlacering[fromX][fromY].getIdentifier() == -1) {
                System.out.println("Identifier of fromClicked is " + brikPlacering[fromX][fromY].getIdentifier());
                return false;
            } else if (fromX == toX && fromY == toY) {
                System.out.println("From and to is the same");
                return false;
            } else if (toX > boardSize - 1 || toX < 0) {
                System.out.println("Out of bounds in direction x");
                return false;
            } else if (toY > boardSize - 1 || toY < 0) {
                System.out.println("Out of bounds in direction y");
                return false;
            } else if (Math.abs(toX - fromX) != Math.abs(toY - fromY)) {
                System.out.println("Move is not diagonal");
                return false;
            } else if ((brikPlacering[toX][toY].getIdentifier() == -1) && (Math.abs(toX - fromX) < 3 && Math.abs(toY - fromY) < 3)) {
                int dirX = fromX > toX ? -1 : 1;
                int dirY = fromY > toY ? -1 : 1;
                if ((brikPlacering[fromX + dirX][fromY + dirY].getIdentifier() != -1)) {
                    if (brikPlacering[fromX][fromY].getOwner().getIdentifier() != brikPlacering[fromX + dirX][fromY + dirY].getOwner().getIdentifier()) {
                        if (!(toX > boardSize - 1 || toX < 0) && !(toY > boardSize - 1 || toY < 0)) { // consider refactoring
                            if ((brikPlacering[toX][toY].getIdentifier() == -1)) {
                                System.out.println("Successfully jumped over opponent piece");
                                brikPlacering[fromX + dirX][fromY + dirY] = new CheckerPiece(new Player("This guy does not exist", -1), 0, -1, new Point(fromX + dirX, fromY + dirY));
                                return true;
                            } else {
                                System.out.println("Able to jump over opponent's piece, but there is another piece behind");
                                return false;
                            }
                        } else {
                            System.out.println("Able to jump over opponent's piece, but location is out of bounds");
                            return false;
                        }
                    } else {
                        System.out.println("The location is viable and there is a target to jump over, but piece is owned by you");
                        return false;
                    }
                }
            } else if (brikPlacering[toX][toY].getIdentifier() != -1) {
                System.out.println("There is another piece at target location");
                return false;
            } else if ((Math.abs(toX - fromX) > 1 || Math.abs(toY - fromY) > 1)) {
                System.out.println("Target location is too far away");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error occurred, returning true by default");
        }
        System.out.println("Nothing else worked, so the move must be legal");
        return true;
    }

    public void setBrikAt(int x, int y, CheckerPiece piece) {
        brikPlacering[x][y] = piece;
    }


    // methods for manipulating fields and creating output
    public void Move(int fromX, int fromY, int toX, int toY) {
        try {
            CheckerPiece pieceClone = brikPlacering[fromX][fromY].clone();
            brikPlacering[fromX][fromY] = brikPlacering[toX][toY];
            brikPlacering[toX][toY] = pieceClone;



        } catch (Exception ex) {
            System.out.println("Error occurred! Give this to the developer: " + ex.getMessage());
        }
    }

    public void populate(Player owner1, Player owner2, Player placeholder) {
        this.player1 = owner1;
        this.player2 = owner2;
        this.currentPlayer = player1;
        IdentifierGenerator id = new IdentifierGenerator();
        if (this.boardSize > 2) {
            for (int yn = 0; yn < boardSize; yn++) {
                // intitializes the board as being full of placeholders
                // this prevents accidental NullPointerException
                for (int xn = 0; xn < boardSize; xn++) {
                    brikPlacering[xn][yn] = new CheckerPiece(placeholder, 0, -1, new Point(xn, yn));
                }
            }
            if (boardSize == 3) {
                // board size is 3
                for (int yn = 0; yn < boardSize / 2; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            brikPlacering[xn][yn] = new CheckerPiece(owner1, 1, id.getNextIdentifier(), new Point(xn, yn));
                        }
                    }
                }
                for (int yn = boardSize / 2 + 1; yn < boardSize; yn++) {
                    // creates each column of player 2's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            brikPlacering[xn][yn] = new CheckerPiece(owner2, 1, id.getNextIdentifier(), new Point(xn, yn));
                        }
                    }
                }
            } else if (boardSize % 2 == 1) {
                // board size is odd and higher than 3
                for (int yn = 0; yn < boardSize / 2 - 1; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 1's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            brikPlacering[xn][yn] = new CheckerPiece(owner1, 1, id.getNextIdentifier(), new Point(xn, yn));
                        }
                    }
                }
                for (int yn = boardSize / 2 + 2; yn < boardSize; yn++) {
                    // creates each column of player 2's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            brikPlacering[xn][yn] = new CheckerPiece(owner2, 1, id.getNextIdentifier(), new Point(xn, yn));
                        }
                    }
                }
            } else {
                // board size is even
                for (int yn = 0; yn < boardSize / 2 - 1; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 1's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            brikPlacering[xn][yn] = new CheckerPiece(owner1, 1, id.getNextIdentifier(), new Point(xn, yn));
                        }
                    }
                }
                for (int yn = boardSize / 2 + 1; yn < boardSize; yn++) {
                    // creates each column of player 2's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            brikPlacering[xn][yn] = new CheckerPiece(owner2, 1, id.getNextIdentifier(), new Point(xn, yn));
                        }
                    }
                }
            }
        }
    }

    public void printBoard() {
        for (int yn = 0; yn < this.boardSize; yn++) {
            System.out.print("[");
            for (int xn = 0; xn < this.boardSize; xn++) {
                if (brikPlacering[xn][yn].getIdentifier() == -1) {
                    System.out.printf("%1$04d ", brikPlacering[xn][yn].getIdentifier());
                } else {
                    System.out.printf("%1$04d ", brikPlacering[xn][yn].getIdentifier());
                }
            }
            System.out.print("]\n");
        }
        System.out.println();
        //System.out.println("Debugging message: All identifiers of value -1 indicates empty placement");
    }

    public CheckerPiece getPieceAtPosition(Point position) {
        return brikPlacering[(int)position.getX()][(int)position.getY()];
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void endTurn() {
        currentPlayer = (currentPlayer == player1 ? player2 : player1);
        int n = 0;
        for (int yn = 0; yn < boardSize; yn++){
            for (int xn = 0; xn < boardSize; xn++){
                if (getBrikPlacering()[xn][yn].getOwner() == currentPlayer){
                    n++;
                }
            }
        }
        if (n==0){
            String winnerMessage = (currentPlayer == player1 ? player2.getPlayerName() : player1.getPlayerName()) + " has won!";
            System.out.println(winnerMessage);

            infoBox(winnerMessage, "Player wins!");
        }
    }

    private static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }



}
