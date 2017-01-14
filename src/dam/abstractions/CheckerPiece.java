package dam.abstractions;

import dam.graphics.AudioPlayer;


/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class CheckerPiece implements Cloneable {
    // fields
    // final because these two should never need to be changed unless the code is faulty anyway
    final private Player Owner;
    final private int Identifier;

    private boolean superPiece;


    // constructors
    public CheckerPiece(Player owner, int id) {
        this.Owner = owner;
        this.Identifier = id;
        this.superPiece = false;

    }

    // methods for returning fields
    public Player getOwner() {
        return this.Owner;
    }


    // the following 3 methods are protected since only this package should be allowed access
    protected int getIdentifier() {
        return this.Identifier;
    }

    protected boolean isSuperPiece() {
        return this.superPiece;
    }

    protected void setSuperPiece(boolean b){
        this.superPiece = b;
    }

    public CheckerPiece clone() {
        try {
            return (CheckerPiece) super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println("ERROR: We couldn't clone one or more pieces");
            new AudioPlayer(AudioPlayer.AUDIO.ERROR);

        }


        return null;
    }
}
