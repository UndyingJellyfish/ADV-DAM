package dam.abstractions;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */
public class IdentifierGenerator {
    // fields
    private int Counter;

    // constructors
    public IdentifierGenerator(int n) {
        this.Counter = n;
    }

    public IdentifierGenerator() {
        this.Counter = 0;
    }

    // methods for returning fields
    public int getNextIdentifier() {
        return this.Counter++;
    }
}
