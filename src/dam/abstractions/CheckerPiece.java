package dam.abstractions;

import java.awt.*;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */
public class CheckerPiece {
    // fields
    private Player Owner;
    private int Identifier;
    private Point Location;
    private boolean superPiece;


    // constructors
    public CheckerPiece(Player owner, int id, Point place, boolean superPiece) {
        this.Owner = owner;
        this.Identifier = id;
        this.Location = place;
        this.superPiece = superPiece;

    }

    // methods for returning fields
    public Player getOwner() {
        return this.Owner;
    }

    public int getIdentifier() {
        return this.Identifier;
    }

    // other types of methods
    public String toString(boolean returnOnlyId) {
        // returns a string containing every property of the current object
        if (returnOnlyId) {
            return Integer.toString(this.getIdentifier());
        }

        return ("This is a checker.\nOwned by: " + this.Owner.getPlayerName() + "\nChecker is identified by " + this.Identifier + " and is located at" + this.Location.toString());
    }

    public Point getLocation(){
        return this.Location;
    }

    public void setLocation(int x, int y){
        this.Location = new Point(x,y);
    }

    public boolean isSuperPiece(){
        return this.superPiece;
    }

    public void setSuperPiece(boolean b){
        this.superPiece = b;
    }

    public CheckerPiece clone() {
        // completely clones the instance of CheckerPiece
        return new CheckerPiece(Owner, Identifier, Location, superPiece);
    }
}
