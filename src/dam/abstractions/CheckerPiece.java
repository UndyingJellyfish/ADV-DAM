package dam.abstractions;

import java.awt.*;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */
public class CheckerPiece {
    // fields
    private Player Owner;
    private int Jumps;
    private int Identifier;
    private Point Location;
    public boolean superPiece;


    // constructors
    public CheckerPiece(Player owner, int jump, int id, Point place, boolean superPiece) {
        this.Owner = owner;
        this.Jumps = jump;
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

    public Point getLocation() {
        return this.Location;
    }

    public void setLocation(int x, int y) {
        this.Location = new Point(x, y);
    }

    public boolean isSuperPiece() {
        return this.superPiece;
    }

    public CheckerPiece clone() {
        // completely clones the instance of CheckerPiece
        return new CheckerPiece(Owner, Jumps, Identifier, Location, superPiece);
    }
}
