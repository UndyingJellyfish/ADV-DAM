package dam.graphics;

import dam.abstractions.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;


/**
 * Created by smous on 02-01-2017.
 */
public class GUIButton extends JButton {

    // number of squares on each side
    private final int N;

    // point object of lastClicked
    private Point lastClicked;
    private GUIBoard.FieldType Owner;
    private Point position;

    // constructors
    public GUIButton(int N, GUIBoard.FieldType field, Point pos) {
        this.N = N;
        this.setOpaque(true);
        this.setBorderPainted(false);
        position = pos;
        Owner = field;

        lastClicked = new Point(-1, -1);
    }

    // methods for returning fields
    public int getN() {
        return this.N;
    }

    public Point getPosition() {
        return position;
    }


    public Point getLastClicked() {
        return this.lastClicked;
    }

    public GUIBoard.FieldType getFieldType() {
        return this.Owner;
    }

    // methods for setting fields
    public void setFieldType(GUIBoard.FieldType newType) {
        this.Owner = newType;
    }

    // methods for graphical interactions
    private void drawImage(String imageName) {


        int PREFERRED_SIZE = (int) Math.floor(this.getWidth() * 0.9);// pixel width and height of a field

        // debugging, remove later
        //JOptionPane.showMessageDialog(null, "Width " + getWidth() + ", height " + getHeight(), "empty title", JOptionPane.INFORMATION_MESSAGE);

        // drawImage function which draws image on button depending on file name and scales to preffered size
        try {
            Image img = ImageIO.read(getClass().getResource(imageName));
            Image newImg = img.getScaledInstance(PREFERRED_SIZE, PREFERRED_SIZE, Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(newImg);
            this.setIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
            this.setIcon(null);
        }
    }

    public void drawField(GUIBoard.FieldType field) {

        // System.out.println("Calling drawField"); // for debugging purposes
        switch (field) {
            case EMPTY:
                // System.out.println("Setting field as EMPTY-type"); //debug message
                this.setIcon(null);

                break;
            case PLAYER0:
                // System.out.println("Setting field as PLAYER0-type"); // debug message
                drawImage("CheckerBlack.png");
                break;
            case PLAYER1:
                //System.out.println("Setting field as PLAYER1-type"); // debug message
                drawImage("CheckerWhite.png");
                break;
            case PLAYER0_KING:
                drawImage("CheckerBlackKing.png");
                break;
            case PLAYER1_KING:
                drawImage("CheckerWhiteKing.png");
                break;
        }


        // colors the grid black and white
        if ((position.getX() % 2 == 0 && position.getY() % 2 == 0) ||
                (position.getX() % 2 == 1 && position.getY() % 2 == 1)) {
            this.setBackground(Color.GRAY);
        } else
            this.setBackground(Color.WHITE);
    }
}