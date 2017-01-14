package dam.graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


/**
 * Created by smous on 02-01-2017.
 * Member primarily responsible for file: Søren Mousten
 */
public class GUIButton extends JButton {

    private GUIBoard.FieldType Owner;
    final private Point Position; // we probably shouldn't start moving the buttons, so this is final

    // constructors
    public GUIButton(GUIBoard.FieldType field, Point pos) {
        this.setOpaque(true);
        this.setBorderPainted(false);
        Position = pos;
        Owner = field;

    }

    // methods for returning fields

    public Point getPosition() {
        return Position;
    }

    public GUIBoard.FieldType getFieldType() {
        return this.Owner;
    }

    // methods for setting fields
    public void setFieldType(GUIBoard.FieldType newType) {
        this.Owner = newType;
        drawField(this.Owner);
    }

    // methods for graphical interactions

    public void drawField(GUIBoard.FieldType field) {
        int PREFERRED_SIZE = (int) Math.floor(this.getWidth() * 0.9);// pixel width and height of a field

        // drawImage function which draws image on button depending on file name and scales to preferred size
        try {
            URL imgResource = getClass().getClassLoader().getResource(field.getPath());
            //System.out.println(field.getPath());

            assert imgResource != null; // this is never possible, so we can safely assert it
            Image img = ImageIO.read(imgResource);

            Image newImg = img.getScaledInstance(PREFERRED_SIZE, PREFERRED_SIZE, Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(newImg);
            this.setIcon(image);
        } catch (IOException e) {
            this.setIcon(null);
        }

        // colors the grid black and white
        if ((Position.getX() % 2 == 0 && Position.getY() % 2 == 0) ||
                (Position.getX() % 2 == 1 && Position.getY() % 2 == 1)) {
            this.setBackground(Color.GRAY);
        } else
            this.setBackground(Color.WHITE);
    }
}