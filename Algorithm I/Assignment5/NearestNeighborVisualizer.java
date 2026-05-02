/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {

        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        // for(int i = 0 ; i < 1e6 ; i++){
        //     Point2D query = new Point2D(StdRandom.uniformDouble(), StdRandom.uniformDouble());
        //     Point2D bruteQuery = brute.nearest(query),
        //             kdtreeQuery = kdtree.nearest(query);  
        //     printUnqeual(bruteQuery, kdtreeQuery, query);
        // }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);
            Point2D bruteQuery = brute.nearest(query),
                    kdtreeQuery = kdtree.nearest(query);
            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.0001);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            bruteQuery.draw();
            StdDraw.setPenRadius(0.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtreeQuery.draw();
            StdDraw.show();
            StdDraw.pause(40);

            printUnqeual(bruteQuery, kdtreeQuery, query);
        }
    }

    private static void printUnqeual(Point2D a, Point2D b, Point2D p){
        if(a.equals(b)) return;
        System.out.println("Unequal at: " + p.x() + " " + p.y());
        if(a.distanceTo(p) < b.distanceTo(p)) System.out.println("brute is correct");
        else System.out.println("kdTree is correct");
    }
}