package dam.logic;

import dam.control.CheckersGame;
import dam.graphics.AudioPlayer;
import dam.graphics.GUIButton;
import dam.menus.GameSetup;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */

public class GameBoard {
    // fields
    // essential parts of the fields
    final private int boardSize; // board size never changes in game
    private CheckerPiece[][] piecePlacement;

    // used for tracking where to move from and to, i.e. how pieces move
    private GUIButton lastClickedGUIButton;
    private GUIButton newClickedGUIButton;

    // used for owners of pieces
    private Player player0;
    private Player player1;
    private Player currentPlayer;


    private int turnsSinceLastKill = 0;

    // logic board constructor
    public GameBoard(CheckerPiece[][] pieces, GameSetup setup) {
        this.piecePlacement = pieces;
        boardSize = setup.boardSize;
    }

    // board essentials
    public void populateBoard(Player owner0, Player owner1, Player placeholder) {
        // this creates all the "logical" game pieces using 3 algorithms

        this.player0 = owner0;
        this.player1 = owner1;
        this.currentPlayer = player0;
        IDGenerator id = new IDGenerator(2);
        if (this.boardSize > 2) {
            for (int yn = 0; yn < boardSize; yn++) {
                // initializes the board as being full of placeholders
                // this prevents accidental NullPointerException's, also allows ButtonListener interactions
                for (int xn = 0; xn < boardSize; xn++) {
                    piecePlacement[xn][yn] = new CheckerPiece(placeholder, -1);
                }
            }
            if (boardSize == 3) {
                // board size is 3
                for (int yn = 0; yn < boardSize / 2; yn++) {
                    // creates each column of player 0's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 0's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            piecePlacement[xn][yn] = new CheckerPiece(owner0, id.getNextIdentifier());
                        }
                    }
                }
                createPlayer1Pieces(owner1, id);
            } else if (boardSize % 2 == 1) {
                // board size is odd and higher than 3
                createPlayer0Pieces(owner0, id);
                for (int yn = boardSize / 2 + 2; yn < boardSize; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < boardSize; xn++) {
                        // creates each row of player 1's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            piecePlacement[xn][yn] = new CheckerPiece(owner1, id.getNextIdentifier());
                        }
                    }
                }
            } else {
                // board size is even
                createPlayer0Pieces(owner0, id);
                createPlayer1Pieces(owner1, id);
            }
        }
    }

    // these two special cases were created because the exact same lines of code was used twice for the same purposes
    // so we refactored them into their own methods, which are then called, this makes populateBoard less of a mess
    private void createPlayer0Pieces(Player owner0, IDGenerator id){
        for (int yn = 0; yn < boardSize / 2 - 1; yn++) {
            // creates each column of player 0's game pieces
            for (int xn = 0; xn < boardSize; xn++) {
                // creates each row of player 0's game pieces
                if ((xn + yn) % 2 == 0) {
                    // only places pieces at every even board field
                    piecePlacement[xn][yn] = new CheckerPiece(owner0, id.getNextIdentifier());
                }
            }
        }
    }

    private void createPlayer1Pieces(Player owner1, IDGenerator id){
        for (int yn = boardSize / 2 + 1; yn < boardSize; yn++) {
            // creates each column of player 1's game pieces
            for (int xn = 0; xn < boardSize; xn++) {
                // creates each row of player 1's game pieces
                if ((xn + yn) % 2 == 0) {
                    // only places pieces at every even board field
                    piecePlacement[xn][yn] = new CheckerPiece(owner1, id.getNextIdentifier());
                }
            }
        }
    }

    // creating console output

    public void printBoard() {
        // used for debugging purposes, prints the identifiers of pieces on logic board
        // this was used to compare graphics and logic, to make sure they were 100 % alike
        // also uses t/f to indicate whether piece is king
        for (int yn = 0; yn < this.boardSize; yn++) {
            System.out.print("[");
            for (int xn = 0; xn < this.boardSize; xn++) {
                System.out.printf("%1$03d", piecePlacement[xn][yn].getIdentifier());
                System.out.printf(piecePlacement[xn][yn].isSuperPiece() ? "t " : "f ");
            }
            System.out.print("]\n");
        }
        System.out.println();
    }


    // methods for returning and setting fields and other properties
    public int getBoardSize(){
        return this.boardSize;
    }

    public CheckerPiece[][] getPiecePlacement() {
        return this.piecePlacement;
    }

    public void setLastClickedGUIButton(GUIButton button) {
        this.lastClickedGUIButton = button;
    }

    public GUIButton getLastClickedGUIButton() {
        return this.lastClickedGUIButton;
    }

    public boolean hasLastClicked() {
        return (lastClickedGUIButton != null);
    }

    public void setNewClickedGUIButton(GUIButton button) {
        this.newClickedGUIButton = button;
    }

    public GUIButton getNewClickedGUIButton() {
        return this.newClickedGUIButton;
    }

    private int countPiecesForPlayer(Player targetPlayer) {
        // counts all pieces for the specified player
        int sum = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (piecePlacement[i][j] != null && (piecePlacement[i][j].getPlayer().getIdentifier() == targetPlayer.getIdentifier()))
                    sum++;
            }
        }
        return sum;
    }

    public CheckerPiece getPieceAtPosition(Point position) {
        return piecePlacement[(int) position.getX()][(int) position.getY()];
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // movement related methods
    private boolean validDirection(int fromY, int toY, Player playerToMove, boolean superPiece) {
        // ensures that non-king (non-super) pieces moves in the correct direction

        boolean playerDirection = (playerToMove == player0);
        boolean moveDirection = (fromY < toY);
        return ((playerDirection == moveDirection) || superPiece);
    }

    public boolean isLegalMove(int fromX, int fromY, int toX, int toY) {
        // used for checking whether it's allowed to move from one location to another
        try {
            if (piecePlacement[fromX][fromY].getIdentifier() == -1) {
                // clicking on a non-player-owned piece
                // System.out.println("Identifier of fromClicked is " + piecePlacement[fromX][fromY].getIdentifier());
                return false;
            } else if (!(validDirection(fromY, toY, piecePlacement[fromX][fromY].getPlayer(), piecePlacement[fromX][fromY].isSuperPiece()))) {
                // clicking on a wrong direction without being king piece
                //System.out.println("Moving backwards is not allowed");
                return false;
            } else if (fromX == toX && fromY == toY) {
                // trying to move to and from the same location
                //System.out.println("From and to is the same");
                return false;
            } else if (toX > boardSize - 1 || toX < 0) {
                // trying to move outside the board along x-axis
                //System.out.println("Out of bounds in direction x");
                return false;
            } else if (toY > boardSize - 1 || toY < 0) {
                // trying to move outside the board along y-axis
                //System.out.println("Out of bounds in direction y");
                return false;
            } else if (Math.abs(toX - fromX) != Math.abs(toY - fromY)) {
                // trying to move non-diagonally
                //System.out.println("move is not diagonal");
                return false;
            } else if (piecePlacement[toX][toY].getIdentifier() != -1) {
                //System.out.println("There is another piece at target location");
                return false;
            } else if (isJumpMove(fromX, fromY, toX, toY)) {
                return true;
            } else if ((Math.abs(toX - fromX) > 1 || Math.abs(toY - fromY) > 1)) {
                //System.out.println("Target location is too far away");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error occurred, returning true by default");
        }
        //System.out.println("Nothing else worked, so the move must be legal");
        return true;
    }

    private boolean isJumpMove(int fromX, int fromY, int toX, int toY) {
        if ((piecePlacement[toX][toY].getIdentifier() == -1) && (Math.abs(toX - fromX) < 3 && Math.abs(toY - fromY) < 3)) {
            // trying to move less than 3 fields to an empty field

            // used for detecting direction of movement, used when checking if jumping over another piece
            int dirX = fromX > toX ? -1 : 1;
            int dirY = fromY > toY ? -1 : 1;
            if ((piecePlacement[fromX + dirX][fromY + dirY].getIdentifier() != -1)) {
                // another piece is between fromLocation and toLocation
                // owners of the piece between and the moving piece are different
                int idFrom = piecePlacement[fromX][fromY].getPlayer().getIdentifier();
                int idDir = piecePlacement[fromX + dirX][fromY + dirY].getPlayer().getIdentifier();
                return idFrom != idDir;
            }
        }
        return false;
    }

    public void move(int fromX, int fromY, int toX, int toY) {
        // used to move pieces in the logic, you should always perform a check with isLegalMove on the same data before
        // attempting to use this method!
        try {
            if (isJumpMove(fromX, fromY, toX, toY)) {
                int dirX = fromX > toX ? -1 : 1;
                int dirY = fromY > toY ? -1 : 1;
                piecePlacement[fromX + dirX][fromY + dirY] = new CheckerPiece(new Player("This guy does not exist", -1),-1);
                turnsSinceLastKill = 0;
            }
            // change places at logic board
            CheckerPiece pieceClone = piecePlacement[fromX][fromY].clone();
            piecePlacement[fromX][fromY] = piecePlacement[toX][toY];
            piecePlacement[toX][toY] = pieceClone;


            // sets as super piece if moving to "end zone"
            int endZone = piecePlacement[toX][toY].getPlayer().getIdentifier() == 1 ? 0 : boardSize - 1;
            piecePlacement[toX][toY].setSuperPiece(toY == endZone);

        } catch (Exception ex) {
            System.out.println("Error occurred! Give this to the developer: " + ex.getMessage());
        }

    }

    private boolean playerHasNoLegalMove(Player player) {
        // used for checking whether a player has any legal moves left
        for (int fromY = 0; fromY < boardSize; fromY++) {
            for (int fromX = 0; fromX < boardSize; fromX++) {
                if ((fromX % 2 == 0 && fromY % 2 == 0) || (fromX % 2 == 1 && fromY % 2 == 1)) {
                    // only checks non-white fields, pieces can only be on black fields
                    if (piecePlacement[fromX][fromY].getPlayer().getIdentifier() == player.getIdentifier()) {
                        // no need to check the legal moves of a piece if it doesn't belong to player we're checking
                        for (int toY = 0; toY < boardSize; toY++) {
                            for (int toX = 0; toX < boardSize; toX++) {
                                if ((toX % 2 == 0 && toY % 2 == 0) || (toX % 2 == 1 && toY % 2 == 1)) {
                                    // only checks non-white fields, pieces can only be on black fields
                                    if (isLegalMove(fromX, fromY, toX, toY)) {
                                        // no need to continue searching when a legal move is found
                                        return false;
                                    }
                                } // end non-white check #2
                            }
                        } // end to-coordinates
                    }
                } // end non-white check #1
            }
        }
        // only returns true if no legal moves exists
        return true;
    }

    public void endTurn() {
        // method to end to turn for each player
        // if current player is player0, then set current player to player1
        currentPlayer = (currentPlayer == player0 ? player1 : player0);

        // winner message shows up when someone has 0 pieces left
        int n = countPiecesForPlayer(currentPlayer);
        if (n == 0) {
            // counts winning players pieces
            currentPlayer = (currentPlayer == player0 ? player1 : player0);
            int piecesLeft = countPiecesForPlayer(getCurrentPlayer());

            // creates winning message
            String winnerMessage = (currentPlayer.getPlayerName()) + " has won with " + piecesLeft + " pieces left!";
            System.out.println(winnerMessage);

            // plays winning sound
            new AudioPlayer(AudioPlayer.AUDIO.WON);
            // prompts a pop-up window notifying that a player has won

            CheckersGame.infoBox(winnerMessage, "Player wins!");

            return;
        }

        if (playerHasNoLegalMove(player0) && playerHasNoLegalMove(player1)) {
            // if no player has any legal moves left, the game is a draw
            String msg = "Game is a draw since no legal moves remain";

            // we don't play winning sounds when both people lose
            CheckersGame.infoBox(msg, "No one wins :(");


        } else if (playerHasNoLegalMove(currentPlayer)) {
            // if the current player has no legal moves, the other player wins
            String winnerMessage = (currentPlayer == player0 ? player1.getPlayerName() : player0.getPlayerName()) + " has won!";
            System.out.println(winnerMessage);

            new AudioPlayer(AudioPlayer.AUDIO.WON);

            CheckersGame.infoBox(winnerMessage, "Player wins!");
        } else if (turnsSinceLastKill == 12) {
            // warn about upcoming force tie
            JOptionPane.showMessageDialog(null, "3 turns until forced draw due to inactive play", "Draw is imminent", JOptionPane.INFORMATION_MESSAGE);
        } else if (turnsSinceLastKill >= 15) {
            // force draw after 15 turns of inactive play
            CheckersGame.infoBox("Game is a draw", "No one wins :(");
        }

        //printBoard();
        turnsSinceLastKill++;

    }

}
