package dam.abstractions;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */
public class IdentifierGenerator {
    // fields
    private int Counter;

    // constructors
    public IdentifierGenerator(int n, int d) {
        this.Counter = n;
    }

    public IdentifierGenerator(int n) {
        this(n, 0);
    }

    public IdentifierGenerator() {
        this.Counter = 0;
    }

    // methods for returning fields
    public int getNextIdentifier() {
        return this.Counter++;
    }

    public String toString() {
        return ("Last identifier: " + this.Counter + "\nDefault identifier: ");
    }

}
