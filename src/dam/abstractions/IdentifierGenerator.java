package dam.abstractions;

/**
 * Created by Emil Damsbo on 04-01-2017.
 */
public class IdentifierGenerator {
    // fields
    private int Counter;
    private int Default;

    // constructors
    public IdentifierGenerator(int n, int d) {
        this.Counter = n;
        this.Default = d;
    }

    public IdentifierGenerator(int n) {
        this(n, 0);
    }

    public IdentifierGenerator() {
        this.Counter = 0;
        this.Default = 0;
    }

    // methods for returning fields
    public int getNextIdentifier() {
        return this.Counter++;
    }

    public int getDefault() {
        return this.Default;
    }

    // methods for setting fields
    public void setIdentifierTo(int n) {
        this.Counter = n;
    }

    public void resetIdentifierCounter() {
        this.Counter = 0;
    }


    public String toString() {
        return ("Last identifier: " + this.Counter + "\nDefault identifier: " + this.Default);
    }

}
