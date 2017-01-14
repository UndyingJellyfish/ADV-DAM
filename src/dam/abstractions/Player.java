package dam.abstractions;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class Player {
    // fields
    final private String PlayerName; // you're not allowed to change name while a game is going on
    final private int Identifier; // changing your identification also seems reckless

    // constructors

    public Player(String name, int id) {
        this.PlayerName = name;
        this.Identifier = id;
    }


    // methods for returning fields
    protected String getPlayerName() {
        return this.PlayerName;
    }

    public int getIdentifier() {
        return this.Identifier;
    }

    public String toString() {
        return ("Player named " + this.PlayerName + " has ID " + this.Identifier);
    }

}
