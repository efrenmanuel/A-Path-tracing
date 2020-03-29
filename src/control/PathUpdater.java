/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import model.Node;
import view.PathPanel;

/**
 *
 * @author efren
 */
public class PathUpdater implements Runnable, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final Timer TIMER;
    private int PIXELSIZE;
    private boolean running;
    private  Node[][] grid;
    private int drawingWith = 1;
    private Point startPoint, endPoint;
    private PathPanel panel;
    
    private int delay=0;

    private List<Node> active, closed;

    public PathUpdater(Node[][] grid, int pixelSize, Timer TIMER, boolean running, PathPanel panel) {
        this.grid = grid;
        this.running = running;
        this.TIMER = TIMER;
        TIMER.start();
        PIXELSIZE = pixelSize;
        this.panel=panel;
    }

    public void setBrush(int newBrush) {
        this.drawingWith = newBrush;
    }

    public void clear() {
        for (int line = grid.length - 1; line >= 0; line--) {

            for (int pixel = 0; pixel < grid[line].length; pixel++) {
                grid[line][pixel].setType(0);
                grid[line][pixel].setCost(0);
            }
        }
        
        startPoint=null;
        endPoint=null;
    }

    public void restart() {
        for (int line = grid.length - 1; line >= 0; line--) {

            for (int pixel = 0; pixel < grid[line].length; pixel++) {
                switch (grid[line][pixel].getType()) {
                    case 1:
                    case 100:
                    case 200:
                        break;
                    default:

                        grid[line][pixel].setType(0);

                }
                grid[line][pixel].setCost(0);
                
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        changePixel(e.getPoint());

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        changePixel(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    @Override
    public void run() {
        restart();
        if (startPoint != null && endPoint != null) {
            active = new ArrayList<>();
            closed = new ArrayList<>();

            boolean arrived = false;

            active.add(grid[startPoint.y][startPoint.x]);
            while (!arrived) {
                Node current = active.get(0);
                if (current.getType() != 100 && current.getType() != 200) {
                    current.setType(3);
                }

                for (int y = -1; y < 2; y++) {
                    if (arrived) {
                        break;
                    }
                    for (int x = -1; x < 2; x++) {
                        if (y == 0 && x == 0) {
                            continue;
                        }
                        //checking if it's in grid
                        if ((current.getPosition().y + y < 0 || current.getPosition().x + x < 0) || (current.getPosition().y + y > grid.length - 1 || current.getPosition().x + x > grid[0].length - 1)) {
                            continue;
                        }
                        Node scanning = grid[current.getPosition().y + y][current.getPosition().x + x];
                        if (closed.contains(scanning)|| scanning.getType() == 1 ||scanning.getType()==100) {
                            continue;
                        }
                        if (scanning.getType() == 200) {
                            arrived = true;
                            break;
                        }
                        scanning.setType(5);
                        try {
                            Thread.sleep(delay*10);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PathUpdater.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        int dx = Math.abs(endPoint.x - scanning.getPosition().x);
                        int dy = Math.abs(endPoint.y - scanning.getPosition().y);

                        double D=1;
                        double D2=1.4;
                        double heuristic = D * (dx + dy) + (D2 - 2 * D) * Integer.min(dx, dy);

                        double step;
                        if (x * y== 0) {
                            step = 1;
                        } else {
                            step = 1.4;
                            System.out.println("diag");
                        }
                        double g =(current.getG() + step);
                        double cost = g + heuristic;
                        if (scanning.getCost() == 0 || g < scanning.getG()) {
                            scanning.setCost(cost);
                            scanning.setG(g);
                            scanning.setH(heuristic);

                            scanning.setOrigin(current);

                            if (!active.contains(scanning)) {
                                active.add(scanning);
                                closed.remove(scanning);
                            }
                        }
                        else if (!closed.contains(scanning) && !active.contains(scanning)){
                            active.add(scanning);
                        }
                        scanning.setType(2);
                    }

                }
                if (arrived) {
                    List<Node> winners = new ArrayList<>();
                    winners.add(current);
                    while (winners.get(winners.size() - 1).getOrigin() != null) {
                        winners.add(winners.get(winners.size() - 1).getOrigin());
                    }
                    winner(winners);
                    break;
                }
                active.remove(current);
                closed.add(current);
                if (current.getOrigin() != null) {
                    current.setType(4);
                }

                Collections.sort(active);
                //System.out.println("active: "+active);
                //System.out.println("removed: "+closed);

            }
        }
    }

    private void winner(List<Node> winners) {
        for (Node winner : winners) {
            if (winner.getType() != 100) {
                winner.setType(300);
            }
        }
    }

    public void changePixel(Point p) {
        Point transformedPoint = new Point(p.x / PIXELSIZE, p.y / PIXELSIZE);
        if (transformedPoint.x >= 0 && transformedPoint.y >= 0 && transformedPoint.x < grid[0].length && transformedPoint.y < grid.length) {
            switch (drawingWith) {
                case 100:
                    if (startPoint != null) {
                        grid[startPoint.y][startPoint.x].setType(0);;
                    }
                    startPoint = new Point(p.x / PIXELSIZE, p.y / PIXELSIZE);
                    break;
                case 200:
                    if (endPoint != null) {
                        grid[endPoint.y][endPoint.x].setType(0);
                    }
                    endPoint = new Point(p.x / PIXELSIZE, p.y / PIXELSIZE);
                    break;
                case 0:
                    switch (grid[startPoint.y][startPoint.x].getType()){
                        case 100:
                            startPoint=null;
                            break;
                        case 200:
                            endPoint=null;
                            break;
                        default:
                            break;
                    }
                default:
                    break;
            }

            grid[p.y / PIXELSIZE][p.x / PIXELSIZE].setType(drawingWith);
        }
    }
    
    public void setDelay(int delay){
        this.delay=delay;
    }

}

/*

G cost = distance from current node to previous+previous.G
H cost = distance to end node sqrt(x^2+y^2)
F = G+H

when F is equal between nodes, H marks priority


 */
