import java.util.Stack;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class PointSET {

    private SET<Point2D> set;

    public PointSET(){
        set = new SET<>();
    }                               // construct an empty set of points 
    public boolean isEmpty(){
        return set.isEmpty();
    }                      // is the set empty? 
    public int size(){
        return set.size();
    }                         // number of points in the set 
    public void insert(Point2D p){
        if( p == null ) throw new IllegalArgumentException();
        set.add(p);
    }              // add the point to the set (if it is not already in the set)
    public boolean contains(Point2D p){
        if( p == null ) throw new IllegalArgumentException();
        return set.contains(p);
    }            // does the set contain point p? 
    public void draw(){
        StdDraw.enableDoubleBuffering();
        //StdDraw.setXscale(0,1);
        //StdDraw.setXscale(0,1);
        for(Point2D p : set){
            StdDraw.point(p.x(),p.y());
        }
        StdDraw.show();
    }                         // draw all points to standard draw 
    public Iterable<Point2D> range(RectHV rect){
        if ( rect == null ) throw new IllegalArgumentException();
        Stack<Point2D> s = new Stack<>();
        for(Point2D p : set){
            if( rect.contains(p) ) s.push(p);
        }
        return s;
    }             // all points that are inside the rectangle (or on the boundary) 
    public Point2D nearest(Point2D p){
        if( p == null ) throw new IllegalArgumentException();
        double dis = Float.MAX_VALUE;
        Point2D nearest = null;
        for(Point2D sp : set){
            if( sp.distanceTo(p) < dis) 
                {
                    nearest = sp;
                    dis = sp.distanceTo(p);
                }
        }
        return nearest;
    }             // a nearest neighbor in the set to point p; null if the set is empty 

    public static void main(String[] args){
        In in = new In(args[0]);
        PointSET set = new PointSET();
        while (!in.isEmpty()) {
            double a = in.readDouble();
            double b = in.readDouble();
            set.insert( new Point2D(a,b) );
        }
        set.draw();
        double c = StdRandom.uniformDouble();
        double d = StdRandom.uniformDouble();
        double e = StdRandom.uniformDouble();
        double f = StdRandom.uniformDouble();
        Point2D p = new Point2D(c,d);
        RectHV rect = new RectHV(Math.min(c,e),Math.min(d,f),Math.max(c,e),Math.max(d,f));
        System.out.println(p);
        System.out.println(set.nearest(p));
        System.out.println("Rect: " + rect);
        System.out.println("Rect Detection:");
        for(Point2D point : set.range(rect)){
            System.out.println(point);
        }
        rect.draw();
        set.draw();
    }                  // unit testing of the methods (optional) 
}