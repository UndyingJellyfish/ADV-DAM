package dam.graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import dam.Control.ButtonListener;
import dam.abstractions.*;
import test.Main;


/**
 * Created by smous on 02-01-2017.
 */
public class GUIButton extends JButton {

    private final int N;
    private Point lastClicked;
    private GUIBoard.FieldType Owner;
    private Point position;
    //private CheckerPiece AssociatedPiece;

    // constructors
    public GUIButton(int N, GUIBoard.FieldType field, Point pos) {
        this.N = N;
        this.setOpaque(true);
        this.setBorderPainted(false);
        //this.AssociatedPiece = piece;
        position = pos;
        Owner = field;
        drawField(field);

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

    public void setFieldType(GUIBoard.FieldType newType){
        this.Owner = newType;
    }

    /*public Point getLocation() {
        return this.getAssociatedPiece().getLocation();
    }*/

    // methods for setting fields
    public void setLastClicked(Point p) {
        try {
            this.lastClicked.x = (int) p.x;
            this.lastClicked.y = (int) p.y;
        } catch (NullPointerException ex) {
            this.lastClicked = new Point(-1, -1);
            System.out.println("Recieved NullPointerException while setting LastClicked");
        }

    }

    // methods for graphics
    private void drawImage(String imageName) {
        // drawImage function which draws image on button depending on file name
        try {
            Image img = ImageIO.read(getClass().getResource(imageName));
            this.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ex){

        }

    }


    public void drawField(GUIBoard.FieldType field) {
        // System.out.println("Calling drawField"); // for debugging purposes
        switch (field) {
            case EMPTY:
                // System.out.println("Setting field as EMPTY-type"); //debug message
                this.setIcon(null);

                break;
            case PLAYER1:
                // System.out.println("Setting field as PLAYER1-type"); // debug message
                drawImage("Checker1.png");
                break;
            case PLAYER2:
                //System.out.println("Setting field as PLAYER2-type"); // debug message
                drawImage("Checker2.png");
                break;
            case PLAYER1DOUBLE:
                drawImage("Checker1King.png");
                break;
            case PLAYER2DOUBLE:
                drawImage("Checker2King.png");
                break;
        }


        // colors the grid black and white
        if ((position.getX() % 2 == 0 && position.getY() % 2 == 0) ||
                (position.getX() % 2 == 1 && position.getY() % 2 == 1)) {
            this.setBackground(Color.BLACK);
        } else
            this.setBackground(Color.WHITE);
    }
}









