/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;

/**
 *
 * @author efren
 */
public class Node implements Comparable<Node> {

    private double cost, g, h;
    private Node origin;
    private final Point position;
    private int type = 0;

    public Node(Point position) {
        this.position = position;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Node getOrigin() {
        return origin;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(cost, o.getCost());
    }

    @Override
    public String toString() {
        return "Node{" + "cost=" + cost + "\n +g=" + g + "\n h=" + h + "\n position=" + position + ", type=" + type + '}';
    }

    public Point getPosition() {
        return position;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

}
