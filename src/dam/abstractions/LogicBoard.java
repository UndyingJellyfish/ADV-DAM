package dam.abstractions;

import dam.graphics.GUIButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */

public class LogicBoard {
    // fields
    private CheckerPiece[][] PiecePlacement;
    private int BoardSize;
    private GUIButton LastClickedGUIButton;
    private GUIButton NewClickedGUIButton;
    private Player Player0;
    private Player Player1;

    private Player CurrentPlayer;


    // constructors
    public LogicBoard(CheckerPiece[][] brikker, int size) {
        this.PiecePlacement = brikker;
        this.BoardSize = size;
    }

    // methods for returning fields and other properties
    public void setLastClickedGUIButton(GUIButton button) {
        this.LastClickedGUIButton = button;
    }

    public GUIButton getLastClickedGUIButton() {
        return this.LastClickedGUIButton;
    }

    public void setNewClickedGUIButton(GUIButton button) {
        this.NewClickedGUIButton = button;
    }

    public GUIButton getNewClickedGUIButton() {
        return this.NewClickedGUIButton;
    }

    public boolean hasLastClicked() {
        return (LastClickedGUIButton != null);
    }

    public CheckerPiece[][] getPiecePlacement() {
        return this.PiecePlacement;
    }

    public int countAllPieces() {
        int sum = 0;
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                if (PiecePlacement[i][j] != null) sum++;
            }
        }
        return sum;
    }

    public int countPiecesForPlayer(Player targetPlayer) {
        int sum = 0;
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                if (PiecePlacement[i][j] != null && (PiecePlacement[i][j].getOwner().getIdentifier() == targetPlayer.getIdentifier()))
                    sum++;
            }
        }
        return sum;
    }

    // methods for manipulating fields and creating output
    public void Move(int fromX, int fromY, int toX, int toY) {
        try {
            CheckerPiece pieceClone = PiecePlacement[fromX][fromY].clone();
            PiecePlacement[fromX][fromY] = PiecePlacement[toX][toY];
            PiecePlacement[toX][toY] = pieceClone;

        } catch (Exception ex) {
            System.out.println("Error occurred! Give this to the developer: " + ex.getMessage());
        }
    }

    private boolean validDirection(int fromY, int toY, Player playerToMove, boolean superPiece) {
        if (superPiece)
            System.out.println("superpiece move");
        boolean playerDirection = (playerToMove == Player0);
        boolean moveDirection = (fromY < toY);
        return ((playerDirection == moveDirection) || superPiece);
    }

    private void isSuperPositionReached(int fromX, int fromY, int toY, Player playerToMove) {
        if (((playerToMove == Player0) &&
                (toY == BoardSize - 1)) ||
                ((playerToMove == Player1) &&
                        (toY == 0))) {
            System.out.println("A piece has achieved super");
            PiecePlacement[fromX][fromY].superPiece = true;
        }
    }

    public boolean isLegalMove(int fromX, int fromY, int toX, int toY) {
        // used for checking whether it's allowed to move from one location to another
        try {
            if (PiecePlacement[fromX][fromY].getIdentifier() == -1) {
                System.out.println("Identifier of fromClicked is " + PiecePlacement[fromX][fromY].getIdentifier());
                return false;
            } else if (!(validDirection(fromY, toY, PiecePlacement[fromX][fromY].getOwner(), PiecePlacement[fromX][fromY].superPiece))) {
                System.out.println("Moving backwards is not allowed");
                return false;
            } else if (fromX == toX && fromY == toY) {
                System.out.println("From and to is the same");
                return false;
            } else if (toX > BoardSize - 1 || toX < 0) {
                System.out.println("Out of bounds in direction x");
                return false;
            } else if (toY > BoardSize - 1 || toY < 0) {
                System.out.println("Out of bounds in direction y");
                return false;
            } else if (Math.abs(toX - fromX) != Math.abs(toY - fromY)) {
                System.out.println("Move is not diagonal");
                return false;
            } else if ((PiecePlacement[toX][toY].getIdentifier() == -1) && (Math.abs(toX - fromX) < 3 && Math.abs(toY - fromY) < 3)) {
                int dirX = fromX > toX ? -1 : 1;
                int dirY = fromY > toY ? -1 : 1;
                if ((PiecePlacement[fromX + dirX][fromY + dirY].getIdentifier() != -1)) {
                    if (PiecePlacement[fromX][fromY].getOwner().getIdentifier() != PiecePlacement[fromX + dirX][fromY + dirY].getOwner().getIdentifier()) {
                        if (!(toX > BoardSize - 1 || toX < 0) && !(toY > BoardSize - 1 || toY < 0)) { // consider refactoring
                            if ((PiecePlacement[toX][toY].getIdentifier() == -1)) {
                                System.out.println("Successfully jumped over opponent piece");
                                PiecePlacement[fromX + dirX][fromY + dirY] = new CheckerPiece(new Player("This guy does not exist", -1), 0, -1, new Point(fromX + dirX, fromY + dirY), false);
                                isSuperPositionReached(fromX, fromY, toY, PiecePlacement[fromX][fromY].getOwner());
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
                // if and not else if to make sure it enters the statement
            }
            if (PiecePlacement[toX][toY].getIdentifier() != -1) {
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
        isSuperPositionReached(fromX, fromY, toY, PiecePlacement[fromX][fromY].getOwner());
        return true;
    }

    public void populate(Player owner0, Player owner1, Player placeholder) {
        this.Player0 = owner0;
        this.Player1 = owner1;
        this.CurrentPlayer = Player0;
        IdentifierGenerator id = new IdentifierGenerator();
        if (this.BoardSize > 2) {
            for (int yn = 0; yn < BoardSize; yn++) {
                // intitializes the board as being full of placeholders
                // this prevents accidental NullPointerException
                for (int xn = 0; xn < BoardSize; xn++) {
                    PiecePlacement[xn][yn] = new CheckerPiece(placeholder, 0, -1, new Point(xn, yn), false);
                }
            }
            if (BoardSize == 3) {
                // board size is 3
                for (int yn = 0; yn < BoardSize / 2; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner0, 1, id.getNextIdentifier(), new Point(xn, yn), false);
                        }
                    }
                }
                for (int yn = BoardSize / 2 + 1; yn < BoardSize; yn++) {
                    // creates each column of player 2's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner1, 1, id.getNextIdentifier(), new Point(xn, yn), false);
                        }
                    }
                }
            } else if (BoardSize % 2 == 1) {
                // board size is odd and higher than 3
                for (int yn = 0; yn < BoardSize / 2 - 1; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 1's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner0, 1, id.getNextIdentifier(), new Point(xn, yn), false);
                        }
                    }
                }
                for (int yn = BoardSize / 2 + 2; yn < BoardSize; yn++) {
                    // creates each column of player 2's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner1, 1, id.getNextIdentifier(), new Point(xn, yn), false);
                        }
                    }
                }
            } else {
                // board size is even
                for (int yn = 0; yn < BoardSize / 2 - 1; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 1's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner0, 1, id.getNextIdentifier(), new Point(xn, yn), false);
                        }
                    }
                }
                for (int yn = BoardSize / 2 + 1; yn < BoardSize; yn++) {
                    // creates each column of player 2's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 2's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner1, 1, id.getNextIdentifier(), new Point(xn, yn), false);
                        }
                    }
                }
            }
        }
    }

    public void printBoard() {
        for (int yn = 0; yn < this.BoardSize; yn++) {
            System.out.print("[");
            for (int xn = 0; xn < this.BoardSize; xn++) {
                System.out.printf("%1$04d ", PiecePlacement[xn][yn].getIdentifier());
            }
            System.out.print("]\n");
        }
        System.out.println();
        //System.out.println("Debugging message: All identifiers of value -1 indicates empty placement");
    }

    public boolean PlayerHasLegalMove(Player player){
        // fix this later
        return false;
    }

    public CheckerPiece getPieceAtPosition(Point position) {
        return PiecePlacement[(int) position.getX()][(int) position.getY()];
    }

    public Player getCurrentPlayer() {
        return CurrentPlayer;
    }

    public void endTurn() {
        CurrentPlayer = (CurrentPlayer == Player0 ? Player1 : Player0);
        int n = countPiecesForPlayer(CurrentPlayer);
        if (n == 0) {
            String winnerMessage = (CurrentPlayer == Player0 ? Player1.getPlayerName() : Player0.getPlayerName()) + " has won!";
            System.out.println(winnerMessage);

            infoBox(winnerMessage, "Player wins!");
        }
    }

    private static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }


}
