package dam.logic;

/**
 * Created by Emil Damsbo on 04-01-2017.
 * Member primarily responsible for file: Emil Damsbo
 */
public class IDGenerator {
    // fields
    private int Counter;

    // constructors
    public IDGenerator(int n) {
        this.Counter = n;
    }

    // methods for returning fields
    public int getNextIdentifier() {
        return this.Counter++;
    }
}
