/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathcalculation;

import control.PathUpdater;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Node;
import view.PathPanel;
import view.PathWindow;
import view.Toolbar;

/**
 *
 * @author efren
 */
public class PathCalculation {

    final static int FIELDWIDTH = 30;
    final static int FIELDHEIGHT = 30;
    final static int PIXELSIZE = 30;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        PathWindow frame = new PathWindow("PathFinder");
        frame.setResizable(false);

        Node[][] grid = new Node[FIELDHEIGHT][FIELDWIDTH];
        for (int line = 0; line < grid.length; line++) {
            for (int column = 0; column < grid.length; column++) {
                grid[line][column]=new Node(new Point(column, line));
                
            }
        }

        PathPanel panel = new PathPanel(FIELDWIDTH, FIELDHEIGHT, PIXELSIZE, grid);

        Toolbar tb = new Toolbar(FIELDWIDTH * PIXELSIZE, panel.getUpdater());
        frame.add(tb);

        frame.add(panel);
        
        frame.pack();
        frame.setVisible(true);

    }

}
