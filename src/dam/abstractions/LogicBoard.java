package dam.abstractions;

import dam.control.CheckersGame;
import dam.graphics.AudioPlayer;
import dam.graphics.GUIButton;
import dam.menus.GameSetup;
import java.awt.*;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */

public class LogicBoard {
    // fields
    // essential parts of the fields
    final private int BoardSize; // board size never changes in game
    private CheckerPiece[][] PiecePlacement;

    // used for tracking where to move from and to, i.e. how pieces move
    private GUIButton LastClickedGUIButton;
    private GUIButton NewClickedGUIButton;

    // used for owners of pieces
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
                    PiecePlacement[xn][yn] = new CheckerPiece(placeholder, -1);
                }
            }
            if (BoardSize == 3) {
                // board size is 3
                for (int yn = 0; yn < BoardSize / 2; yn++) {
                    // creates each column of player 0's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 0's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner0, id.getNextIdentifier());
                        }
                    }
                }
                CreatePlayer1Pieces(owner1, id);
            } else if (BoardSize % 2 == 1) {
                // board size is odd and higher than 3
                CreatePlayer0Pieces(owner0, id);
                for (int yn = BoardSize / 2 + 2; yn < BoardSize; yn++) {
                    // creates each column of player 1's game pieces
                    for (int xn = 0; xn < BoardSize; xn++) {
                        // creates each row of player 1's game pieces
                        if ((xn + yn) % 2 == 0) {
                            // only places pieces at every even board field
                            PiecePlacement[xn][yn] = new CheckerPiece(owner1, id.getNextIdentifier());
                        }
                    }
                }
            } else {
                // board size is even
                CreatePlayer0Pieces(owner0, id);
                CreatePlayer1Pieces(owner1, id);
            }
        }
    }

    // these two special cases were created because the exact same lines of code was used twice for the same purposes
    // so we refactored them into their own methods, which are then called, this makes PopulateBoard less of a mess
    private void CreatePlayer0Pieces(Player owner0, IdentifierGenerator id){
        for (int yn = 0; yn < BoardSize / 2 - 1; yn++) {
            // creates each column of player 0's game pieces
            for (int xn = 0; xn < BoardSize; xn++) {
                // creates each row of player 0's game pieces
                if ((xn + yn) % 2 == 0) {
                    // only places pieces at every even board field
                    PiecePlacement[xn][yn] = new CheckerPiece(owner0, id.getNextIdentifier());
                }
            }
        }
    }

    private void CreatePlayer1Pieces(Player owner1, IdentifierGenerator id){
        for (int yn = BoardSize / 2 + 1; yn < BoardSize; yn++) {
            // creates each column of player 1's game pieces
            for (int xn = 0; xn < BoardSize; xn++) {
                // creates each row of player 1's game pieces
                if ((xn + yn) % 2 == 0) {
                    // only places pieces at every even board field
                    PiecePlacement[xn][yn] = new CheckerPiece(owner1, id.getNextIdentifier());
                }
            }
        }
    }

    // creating console output

    public void PrintBoard() {
        // used for debugging purposes, prints the identifiers of pieces on logic board
        // this was used to compare graphics and logic, to make sure they were 100 % alike
        // also uses t/f to indicate whether piece is king
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
    public int getBoardSize(){
        return this.BoardSize;
    }

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

    public boolean isLegalMove(int fromX, int fromY, int toX, int toY) {
        // used for checking whether it's allowed to move from one location to another
        try {
            if (PiecePlacement[fromX][fromY].getIdentifier() == -1) {
                // clicking on a non-player-owned piece
                // System.out.println("Identifier of fromClicked is " + PiecePlacement[fromX][fromY].getIdentifier());
                return false;
            } else if (!(validDirection(fromY, toY, PiecePlacement[fromX][fromY].getOwner(), PiecePlacement[fromX][fromY].isSuperPiece()))) {
                // clicking on a wrong direction without being king piece
                //System.out.println("Moving backwards is not allowed");
                return false;
            } else if (fromX == toX && fromY == toY) {
                // trying to move to and from the same location
                //System.out.println("From and to is the same");
                return false;
            } else if (toX > BoardSize - 1 || toX < 0) {
                // trying to move outside the board along x-axis
                //System.out.println("Out of bounds in direction x");
                return false;
            } else if (toY > BoardSize - 1 || toY < 0) {
                // trying to move outside the board along y-axis
                //System.out.println("Out of bounds in direction y");
                return false;
            } else if (Math.abs(toX - fromX) != Math.abs(toY - fromY)) {
                // trying to move non-diagonally
                //System.out.println("Move is not diagonal");
                return false;
            } else if (PiecePlacement[toX][toY].getIdentifier() != -1) {
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
        if ((PiecePlacement[toX][toY].getIdentifier() == -1) && (Math.abs(toX - fromX) < 3 && Math.abs(toY - fromY) < 3)) {
            // trying to move less than 3 fields to an empty field

            // used for detecting direction of movement, used when checking if jumping over another piece
            int dirX = fromX > toX ? -1 : 1;
            int dirY = fromY > toY ? -1 : 1;
            if ((PiecePlacement[fromX + dirX][fromY + dirY].getIdentifier() != -1)) {
                // another piece is between fromLocation and toLocation
                // owners of the piece between and the moving piece are different
                int idFrom = PiecePlacement[fromX][fromY].getOwner().getIdentifier();
                int idDir = PiecePlacement[fromX + dirX][fromY + dirY].getOwner().getIdentifier();
                return idFrom != idDir;
            }
        }
        return false;
    }

    public void Move(int fromX, int fromY, int toX, int toY) {
        // used to move pieces in the logic, you should always perform a check with isLegalMove on the same data before
        // attempting to use this method!
        try {
            if (isJumpMove(fromX, fromY, toX, toY)) {
                int dirX = fromX > toX ? -1 : 1;
                int dirY = fromY > toY ? -1 : 1;
                PiecePlacement[fromX + dirX][fromY + dirY] = new CheckerPiece(new Player("This guy does not exist", -1),-1);

            }
            // change places at logic board
            CheckerPiece pieceClone = PiecePlacement[fromX][fromY].clone();
            PiecePlacement[fromX][fromY] = PiecePlacement[toX][toY];
            PiecePlacement[toX][toY] = pieceClone;


            // sets as super piece if moving to "end zone"
            int endZone = PiecePlacement[toX][toY].getOwner().getIdentifier() == 1 ? 0 : BoardSize - 1;
            PiecePlacement[toX][toY].setSuperPiece(toY == endZone);

        } catch (Exception ex) {
            System.out.println("Error occurred! Give this to the developer: " + ex.getMessage());
        }

    }

    public boolean PlayerHasLegalMove(Player player) {
        // used for checking is a player has any legal moves left
        for (int fromY = 0; fromY < BoardSize; fromY++) {
            for (int fromX = 0; fromX < BoardSize; fromX++) {
                if ((fromX % 2 == 0 && fromY % 2 == 0) || (fromX % 2 == 1 && fromY % 2 == 1)) {
                    // only checks non-white fields
                    if (PiecePlacement[fromX][fromY].getOwner().getIdentifier() == player.getIdentifier()) {
                        // no need to check the legal moves of a button if it doesn't belong to player we're checking
                        for (int toY = 0; toY < BoardSize; toY++) {
                            for (int toX = 0; toX < BoardSize; toX++) {
                                if ((toX % 2 == 0 && toY % 2 == 0) || (toX % 2 == 1 && toY % 2 == 1)) {
                                    // only checks non-white fields
                                    if (isLegalMove(fromX, fromY, toX, toY)) {
                                        // no need to continue searching when a legal move is found
                                        return true;
                                    }
                                }
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
            // counts winning players pieces
            CurrentPlayer = (CurrentPlayer == Player0 ? Player1 : Player0);
            int piecesLeft = countPiecesForPlayer(getCurrentPlayer());

            // creates winning message
            String winnerMessage = (CurrentPlayer.getPlayerName()) + " has won with " + piecesLeft + " pieces left!";
            System.out.println(winnerMessage);

            // plays winning sound
            new AudioPlayer(AudioPlayer.AUDIO.WON);
            // prompts a pop-up window notifying that a player has won

            CheckersGame.infoBox(winnerMessage, "Player wins!");

            return;
        }

        if (!PlayerHasLegalMove(Player0) && !PlayerHasLegalMove(Player1)) {
            // if no player has any legal moves left, the game is a draw
            String msg = "Game is a draw";
            System.out.println("No legal moves remain: " + msg);

            CheckersGame.infoBox(msg, "No one wins :(");


        } else if (!PlayerHasLegalMove(CurrentPlayer)) {
            // if the current player has no legal moves, the other player wins
            String winnerMessage = (CurrentPlayer == Player0 ? Player1.getPlayerName() : Player0.getPlayerName()) + " has won!";
            System.out.println(winnerMessage);

            CheckersGame.infoBox(winnerMessage, "Player wins!");

            new AudioPlayer(AudioPlayer.AUDIO.WON);
        }

        PrintBoard();

    }

}
