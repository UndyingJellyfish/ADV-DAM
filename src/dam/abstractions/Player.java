package dam.abstractions;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class Player {
    // fields
    private String PlayerName;
    private int Identifier;

    // constructors

    public Player(String name, int id) {
        this.PlayerName = name;
        this.Identifier = id;
    }

    public Player(String name) {
        this(name, 0);
    }

    public Player(int id) {
        this(("player " + id), id);
    }

    // methods for returning fields
    public String getPlayerName() {
        return this.PlayerName;
    }

    public int getIdentifier() {
        return this.Identifier;
    }

    public String toString() {
        return ("Player named " + this.PlayerName + " has ID " + this.Identifier);
    }

}
