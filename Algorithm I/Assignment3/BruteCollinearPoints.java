import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {

    private Stack<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points)
    {
        lineSegments = new Stack<>();
        Arrays.sort(points);
        if(points == null) throw new IllegalArgumentException();
        for(Point p : points) if(p == null) throw new IllegalArgumentException(); //check null
        
        for(int i = 0; i < points.length; i++){
            for(int j = i+1 ; j < points.length; j++)
                { 
                    if(points[j] == points[i]) throw new IllegalArgumentException(); //check repeat points
                    double s1 = points[i].slopeTo(points[j]);
                    for(int k = j+1 ; k < points.length; k++ )
                    { 
                        if(points[k] == points[j]) throw new IllegalArgumentException(); //check repeat points
                        double s2 = points[i].slopeTo(points[k]);
                        for(int l = k+1 ; l < points.length; l++ )
                        { 
                            if(points[l] == points[k]) throw new IllegalArgumentException(); //check repeat points
                            double s3 = points[i].slopeTo(points[l]);
                            if(s1==s2 && s2 == s3) 
                                {
                                    Point[] p = new Point[4];
                                    p[0] = points[i];
                                    p[1] = points[j];
                                    p[2] = points[k];
                                    p[3] = points[l];
                                    Arrays.sort(p);
                                    lineSegments.push( new LineSegment ( p[0],p[3] ) );
                                    // System.out.println("Added Line: " + (new LineSegment(p[0],p[3])).toString() );
                                    // System.out.println(s1 + " " + s2 + " " + s3 );
                                    // System.out.println(points[i] + " " + points[j] + " " + points[k] + " " + points[l] );                                    
                                }
                        }
                    }
                }
        }
    }
    
    public int numberOfSegments()
    {
        return lineSegments.size();
    }        
    public LineSegment[] segments()
    {
        LineSegment[] r = new LineSegment[lineSegments.size()];
        int i = 0;
        for(LineSegment l : lineSegments){
            r[i++] = l;
        } 
        
        return r;
    }      
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }          
}