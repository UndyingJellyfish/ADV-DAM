package dam.logic;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class Player {
    // fields
    final private String playerName; // you're not allowed to change name while a game is going on
    final private int identifier; // changing your identification also seems reckless

    // constructors

    public Player(String name, int id) {
        this.playerName = name;
        this.identifier = id;
    }


    // methods for returning fields
    public String getPlayerName() {
        return this.playerName;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public String toString() {
        return ("Player named " + this.playerName + " has ID " + this.identifier);
    }

}
