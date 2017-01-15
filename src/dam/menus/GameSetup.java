package dam.menus;

/**
 * Created by smous on 11-01-2017.
 * Member primarily responsible for file: Søren Mousten
 */
public class GameSetup {
    public int boardSize;
    public GameSetup(int BoardSize) {
        this.boardSize = BoardSize;
    }

    // enumeration to keep the different frame sizes.
    public enum FrameSize {
        MIN_SIZE,
        MED_SIZE,
        MAX_SIZE
    }

    // the default frame size is set to the max size
    public FrameSize frameSize = FrameSize.MAX_SIZE;
}
