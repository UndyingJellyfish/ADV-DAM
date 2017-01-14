package dam.abstractions;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class IdentifierGenerator {
    // fields
    private int Counter;

    // constructors
    public IdentifierGenerator(int n) {
        this.Counter = n;
    }

    // methods for returning fields
    public int getNextIdentifier() {
        return this.Counter++;
    }
}
