package dam.abstractions;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */
public class Player {
    // fields
    private String PlayerName;
    private int Identifier;
    private double Score;

    // constructors
    public Player(String name, int id, double score) {
        this.PlayerName = name;
        this.Identifier = id;
        this.Score = score;
    }

    public Player(String name, int id) {
        this(name, id, 0.0);
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

    public double getScore() {
        return this.Score;
    }

    public String toString() {
        return ("Player named " + this.PlayerName + " has ID " + this.Identifier + "and score " + this.Score);
    }

}
