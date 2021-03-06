package dam.logic;

import dam.graphics.AudioPlayer;


/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class CheckerPiece implements Cloneable {
    // fields
    // final because these two should never need to be changed unless the code is faulty anyway
    final private Player player;
    final private int identifier;
    private boolean superPiece;


    // constructors
    public CheckerPiece(Player player, int id) {
        this.player = player;
        this.identifier = id;
        this.superPiece = false;

    }

    // methods for returning fields
    public Player getPlayer() {
        return this.player;
    }


    // the following 3 methods are protected since only this package should be allowed access
    protected int getIdentifier() {
        return this.identifier;
    }

    public boolean isSuperPiece() {
        return this.superPiece;
    }

    public void setSuperPiece(boolean b){
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
