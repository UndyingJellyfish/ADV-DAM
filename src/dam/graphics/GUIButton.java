package dam.graphics;

import dam.abstractions.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;


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

    public GUIBoard.FieldType getFieldType() {
        return this.Owner;
    }

    // methods for setting fields
    public void setFieldType(GUIBoard.FieldType newType) {
        this.Owner = newType;
    }

    // methods for graphical interactions
    private void drawImage(GUIBoard.FieldType field) {
        int PREFERRED_SIZE = (int) Math.floor(this.getWidth() * 0.9);// pixel width and height of a field

        // drawImage function which draws image on button depending on file name and scales to preffered size
        try {
            Image img = ImageIO.read(new File(field.getText()));
            Image newImg = img.getScaledInstance(PREFERRED_SIZE, PREFERRED_SIZE, Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(newImg);
            this.setIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
            this.setIcon(null);
        }
    }

    public void drawField(GUIBoard.FieldType field) {
        drawImage(field);


        // colors the grid black and white
        if ((position.getX() % 2 == 0 && position.getY() % 2 == 0) ||
                (position.getX() % 2 == 1 && position.getY() % 2 == 1)) {
            this.setBackground(Color.GRAY);
        } else
            this.setBackground(Color.WHITE);
    }
}