package com.ufpa.physics;

import java.awt.geom.Point2D;

public class Magnet{
    private Point2D positive;
    private Point2D negative;

    public Magnet(Point2D positive, Point2D negative){
        this.positive = positive;
        this.negative = negative;
    }

    public Point2D getPositive(){return positive;}
    public Point2D getNegative(){return negative;}
    public void setPositive(Point2D p){
        this.positive = p;
    }
    public void setNegative(Point2D p){
        this.negative = p;
    }
}
