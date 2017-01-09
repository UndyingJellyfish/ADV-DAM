package dam.abstractions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */
public class CheckerPiece {
    // fields
    private Player Owner;
    private int Jumps;
    private int Identifier;
    private Point Location;


    // constructors
    public CheckerPiece(Player owner, int jump, int id, Point place) {
        this.Owner = owner;
        this.Jumps = jump;
        this.Identifier = id;
        this.Location = place;

    }

    public CheckerPiece(Player owner, int jump, int id) {
        this(owner, jump, id, new Point(0, 0));
    }

    public CheckerPiece(Player owner, int jump) {
        this(owner, jump, 0);
    }

    public CheckerPiece(Player owner) {
        this(owner, 1);
    }

    public CheckerPiece() {
        this(new Player("unnamed player"));
    }

    // methods for returning fields
    public Player getOwner() {
        return this.Owner;
    }

    public int getJumps() {
        return this.Jumps;
    }

    public int getIdentifier() {
        return this.Identifier;
    }

    public Point getLocation() {
        return this.Location;
    }

    // methods for setting fields
    public void setOwner(Player o) {
        this.Owner = o;
    }

    public void setJumps(int j) {
        this.Jumps = j;
    }

    public void setId(int i) {
        this.Identifier = i;
    }

    public void setLocation(Point p) {
        this.Location.x = p.x;
        this.Location.y = p.y;
    }


    // other types of methods
    public String toString(boolean returnOnlyId) {
        // returns a string containing every property of the current object
        if (returnOnlyId) {
            return Integer.toString(this.getIdentifier());
        }

        return ("This is a checker.\nOwned by: " + this.Owner.getPlayerName() + "\nChecker has " + this.Jumps + " jumps.\nChecker is identified by " + this.Identifier + " and is located at" + this.Location.toString());
    }

    public CheckerPiece clone() {
        // completely clones the instance of CheckerPiece
        return new CheckerPiece(Owner, Jumps, Identifier, Location);
    }

    public CheckerPiece cloneToLocation(Point p) {
        // clones the current instance of CheckerPiece to a new location
        return new CheckerPiece(Owner, Jumps, Identifier, p);
    }
}
