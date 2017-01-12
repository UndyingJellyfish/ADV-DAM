package dam.abstractions;

import dam.Control.CheckersGame;
import dam.graphics.AudioPlayer;
import dam.graphics.GUIButton;
import dam.menus.GameSetup;
import org.omg.CORBA.Current;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */

public class LogicBoard {
    // fields
    private CheckerPiece[][] PiecePlacement;
    private GUIButton LastClickedGUIButton;
    private GUIButton NewClickedGUIButton;
    private int BoardSize;
    private Player Player0;
    private Player Player1;
    private Player CurrentPlayer;

    // logic board constructor
    public LogicBoard(CheckerPiece[][] pieces, GameSetup setup) {
        this.PiecePlacement = pieces;
        BoardSize = setup.boardSquares;
    }

    // board essentials
    public void PopulateBoard(Player owner0, Player owner1, Player placeholder) {
        // this creates all the "logical" game pieces using 3 algorithms

        this.Player0 = owner0;
        this.Player1 = owner1;
        this.CurrentPlayer = Player0;
        IdentifierGenerator id = new IdentifierGenerator(2);
        if (this.BoardSize > 2) {
            for (int yn = 0; yn < BoardSize; yn++) {
                // initializes the board as being full of placeholders
                // this prevents accidental NullPointerException's, also allows ButtonListener interactions
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

    // creating console output
    public void printBoard() {
        // prints the board identifiers in the console, also uses t/f to indicate whether piece is king
        for (int yn = 0; yn < this.BoardSize; yn++) {
            System.out.print("[");
            for (int xn = 0; xn < this.BoardSize; xn++) {
                System.out.printf("%1$03d", PiecePlacement[xn][yn].getIdentifier());
                System.out.printf(PiecePlacement[xn][yn].isSuperPiece() ? "t " : "f ");
            }
            System.out.print("]\n");
        }
        System.out.println();
    }

    // methods for returning and setting fields and other properties
    public CheckerPiece[][] getPiecePlacement() {
        return this.PiecePlacement;
    }

    public void setLastClickedGUIButton(GUIButton button) {
        this.LastClickedGUIButton = button;
    }

    public GUIButton getLastClickedGUIButton() {
        return this.LastClickedGUIButton;
    }

    public boolean hasLastClicked() {
        return (LastClickedGUIButton != null);
    }

    public void setNewClickedGUIButton(GUIButton button) {
        this.NewClickedGUIButton = button;
    }

    public GUIButton getNewClickedGUIButton() {
        return this.NewClickedGUIButton;
    }

    public int countPiecesForPlayer(Player targetPlayer) {
        // counts all pieces for the specified player
        int sum = 0;
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                if (PiecePlacement[i][j] != null && (PiecePlacement[i][j].getOwner().getIdentifier() == targetPlayer.getIdentifier()))
                    sum++;
            }
        }
        return sum;
    }

    public ArrayList<Point> legalMoves(CheckerPiece gamePiece) {
        int x = (int) gamePiece.getLocation().getX();
        int y = (int) gamePiece.getLocation().getY();
        ArrayList<Point> moves = new ArrayList<>();
        for (int yn = 0; yn < BoardSize; yn++) {
            for (int xn = 0; xn < BoardSize; xn++) {
                if (isLegalMovePrivateUseOnly(x, y, xn, yn)) moves.add(new Point(xn, yn));
            }
        }

        return moves;
    }

    public CheckerPiece getPieceAtPosition(Point position) {
        return PiecePlacement[(int) position.getX()][(int) position.getY()];
    }

    public Player getCurrentPlayer() {
        return CurrentPlayer;
    }

    // movement related methods
    private boolean validDirection(int fromY, int toY, Player playerToMove, boolean superPiece) {
        // ensures that non-king (non-super) pieces moves in the correct direction
        boolean playerDirection = (playerToMove == Player0);
        boolean moveDirection = (fromY < toY);
        return ((playerDirection == moveDirection) || superPiece);
    }

    private void isSuperPositionReached(int fromX, int fromY, int toY, Player playerToMove) {
        // used for checking whether a piece has reached the state of "king" or "super"
        if (((playerToMove == Player0) &&
                (toY == BoardSize - 1)) ||
                ((playerToMove == Player1) &&
                        (toY == 0))) {
            //System.out.println("A piece has achieved super");
            PiecePlacement[fromX][fromY].superPiece = true;
        }
    }

    public boolean isLegalMove(int fromX, int fromY, int toX, int toY) {
        // used for checking whether it's allowed to move from one location to another
        try {
            if (PiecePlacement[fromX][fromY].getIdentifier() == -1) {
                // clicking on a non-player-owned piece
                System.out.println("Identifier of fromClicked is " + PiecePlacement[fromX][fromY].getIdentifier());
                return false;
            } else if (!(validDirection(fromY, toY, PiecePlacement[fromX][fromY].getOwner(), PiecePlacement[fromX][fromY].superPiece))) {
                // clicking on a wrong direction without being king piece
                System.out.println("Moving backwards is not allowed");
                return false;
            } else if (fromX == toX && fromY == toY) {
                // trying to move to and from the same location
                System.out.println("From and to is the same");
                return false;
            } else if (toX > BoardSize - 1 || toX < 0) {
                // trying to move outside the board along x-axis
                System.out.println("Out of bounds in direction x");
                return false;
            } else if (toY > BoardSize - 1 || toY < 0) {
                // trying to move outside the board along y-axis
                System.out.println("Out of bounds in direction y");
                return false;
            } else if (Math.abs(toX - fromX) != Math.abs(toY - fromY)) {
                // trying to move non-diagonally
                System.out.println("Move is not diagonal");
                return false;
            } else if ((PiecePlacement[toX][toY].getIdentifier() == -1) && (Math.abs(toX - fromX) < 3 && Math.abs(toY - fromY) < 3)) {
                // trying to move less than 3 fields to an empty field

                // used for detecting direction of movement, used when checking if jumping over another piece
                int dirX = fromX > toX ? -1 : 1;
                int dirY = fromY > toY ? -1 : 1;
                if ((PiecePlacement[fromX + dirX][fromY + dirY].getIdentifier() != -1)) {
                    // another piece is between fromLocation and toLocation
                    if (PiecePlacement[fromX][fromY].getOwner().getIdentifier() != PiecePlacement[fromX + dirX][fromY + dirY].getOwner().getIdentifier()) {
                        // owners of the piece between and the moving piece are different
                        System.out.println("Successfully jumped over opponent piece");

                        PiecePlacement[fromX + dirX][fromY + dirY] = new CheckerPiece(new Player("This guy does not exist", -1), 0, -1, new Point(fromX + dirX, fromY + dirY), false);
                        isSuperPositionReached(fromX, fromY, toY, PiecePlacement[fromX][fromY].getOwner());
                        // move this part up to the move() method after checking whether you jumped over an opponent piece

                        return true;
                    } else {
                        System.out.println("The location is viable and there is a target to jump over, but piece is owned by you");
                        return false;
                    }
                }
            }
            // if and not else if to make sure it enters the statement
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

    // TODO: Remove this after refactoring other isLegalMove
    private boolean isLegalMovePrivateUseOnly(int fromX, int fromY, int toX, int toY) {
        // used for checking whether it's allowed to move from one location to another
        try {
            if (PiecePlacement[fromX][fromY].getIdentifier() == -1) {
                // clicking on a non-player-owned piece
                return false;
            } else if (!(validDirection(fromY, toY, PiecePlacement[fromX][fromY].getOwner(), PiecePlacement[fromX][fromY].superPiece))) {
                // clicking on a wrong direction without being king piece
                return false;
            } else if (fromX == toX && fromY == toY) {
                // trying to move to and from the same location
                return false;
            } else if (toX > BoardSize - 1 || toX < 0) {
                // trying to move outside the board along x-axis
                return false;
            } else if (toY > BoardSize - 1 || toY < 0) {
                // trying to move outside the board along y-axis
                return false;
            } else if (Math.abs(toX - fromX) != Math.abs(toY - fromY)) {
                // trying to move non-diagonally
                return false;
            } else if ((PiecePlacement[toX][toY].getIdentifier() == -1) && (Math.abs(toX - fromX) < 3 && Math.abs(toY - fromY) < 3)) {
                // trying to move less than 3 fields to an empty field

                // used for detecting direction of movement, used when checking if jumping over another piece
                int dirX = fromX > toX ? -1 : 1;
                int dirY = fromY > toY ? -1 : 1;
                if ((PiecePlacement[fromX + dirX][fromY + dirY].getIdentifier() != -1)) {
                    // another piece is between fromLocation and toLocation
                    if (PiecePlacement[fromX][fromY].getOwner().getIdentifier() != PiecePlacement[fromX + dirX][fromY + dirY].getOwner().getIdentifier()) {
                        // owners of the piece between and the moving piece are different
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            // if and not else if to make sure it enters the statement
            if (PiecePlacement[toX][toY].getIdentifier() != -1) {
                return false;
            } else if ((Math.abs(toX - fromX) > 1 || Math.abs(toY - fromY) > 1)) {
                return false;
            }
        } catch (Exception e) {
            // do nothing
        }
        return true;
    }

    public void Move(int fromX, int fromY, int toX, int toY) {
        // used to move pieces in the logic, you should always perform a check with isLegalMove on the same data before
        // attempting to use this method!
        try {
            CheckerPiece pieceClone = PiecePlacement[fromX][fromY].clone();
            PiecePlacement[fromX][fromY] = PiecePlacement[toX][toY];
            PiecePlacement[toX][toY] = pieceClone;


            PiecePlacement[fromX][fromY].setLocation(toX, toY);
            PiecePlacement[toX][toY].setLocation(fromX, fromY);


        } catch (Exception ex) {
            System.out.println("Error occurred! Give this to the developer: " + ex.getMessage());
        }

    }

    public boolean PlayerHasLegalMove(Player player) {
        // used for checking if a player has any legal moves left

        // TODO: consider refactoring to check for only black pieces
        for (int fromY = 0; fromY < BoardSize; fromY++) {
            for (int fromX = 0; fromX < BoardSize; fromX++) {
                if (PiecePlacement[fromX][fromY].getOwner().getIdentifier() == player.getIdentifier()) {
                    // no need to check the legal moves of a button if it doesn't belong to the player we're checking
                    for (int toY = 0; toY < BoardSize; toY++) {
                        for (int toX = 0; toX < BoardSize; toX++) {
                            if (isLegalMovePrivateUseOnly(fromX, fromY, toX, toY)) {
                                // only increments if a given move is legal
                                // as soon as we know that a legal move exists we don't need to continue
                                return true;
                            }
                        }
                    }
                }
            }
        }

        // only returns false if no legal moves are found
        return false;
    }


    public void endTurn() {
        // method to end to turn for each player
        // if current player is player0, then set current player to player1
        CurrentPlayer = (CurrentPlayer == Player0 ? Player1 : Player0);

        // winner message shows up when someone has 0 pieces left
        int n = countPiecesForPlayer(CurrentPlayer);
        if (n == 0) {
            int piecesLeft = Math.max(countPiecesForPlayer(Player0), countPiecesForPlayer(Player1));

            String winnerMessage = (CurrentPlayer == Player0 ? Player1.getPlayerName() : Player0.getPlayerName()) + " has won with " + piecesLeft + " pieces left!";
            System.out.println(winnerMessage);

            // plays winning sound
            new AudioPlayer(AudioPlayer.AUDIO.WON);
            // prompts a pop-up window notifying that a player has won
            CheckersGame.infoBox(winnerMessage, "Player wins!");
            return;
        }

        int temp = 0;

        // count the amount of legal moves left
        for (int yn = 0; yn < BoardSize; yn++) {
            for (int xn = 0; xn < BoardSize; xn++) {
                temp += legalMoves(PiecePlacement[xn][yn]).size();
            }
        }


        if (temp == 0) {
            // if no player has any legal moves left, the game is a draw
            String msg = "Game is a draw";
            System.out.println("No legal moves remain:" + msg);

            CheckersGame.infoBox(msg, "Player wins!");
            return;
        }

        if (!PlayerHasLegalMove(CurrentPlayer)) {
            // if the current player has no legal moves, the other player wins
            String winnerMessage = (CurrentPlayer == Player0 ? Player1.getPlayerName() : Player0.getPlayerName()) + " has won!";
            System.out.println(winnerMessage);
            new AudioPlayer(AudioPlayer.AUDIO.WON);
            CheckersGame.infoBox(winnerMessage, "Player wins!");

            return;
        }


    }

}
