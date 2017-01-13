package dam.abstractions;

import java.awt.*;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
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

    public void setLocation(int x, int y) {
        this.Location = new Point(x, y);
    }

    public boolean isSuperPiece() {
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
