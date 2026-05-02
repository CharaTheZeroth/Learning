import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {

    private Stack<LineSegment> lineSegments;
    private Point[] pivot;

    public FastCollinearPoints(Point[] points){
        lineSegments = new Stack<>();
        //Copy points[] to determiate the order of selected pivot
        pivot = points.clone();
        int n = points.length;
        //sort the array by slope
        for(Point p : points) if(p == null) throw new IllegalArgumentException();
        Arrays.sort(points); // sort by coords
        for(int i = 1; i < n; i++) if(points[i].compareTo(points[i-1]) == 0) throw new IllegalArgumentException();// check points repeating
        if(points.length < 4) return;
        //for(Point p : points)System.out.println(p.toString());
        for(int i = 0; i < n; i++){
            Arrays.sort(points,pivot[i].slopeOrder()); // sort by slope to each pivot[i]
            int base = 1;   //base is the current slope provider
            for(int j = 1; j < n ; j++){
                //if p[j]'s slope no longer equals to that of the base,
                //then the same ones are from base to j-1 along with p[0]
                //points orders are fine because Arrays.sort() is stable //<-- this is wrong, due to mulisorted by slopes.To fix this , sort again before sort by slope.
                //check whether point count >= 4, compare max and min to p[0] since p[0]'s coords are not sorted,
                //push the lineSegment and move base to j.
                if( points[0].slopeTo( points[base] ) != points[0].slopeTo( points[j] ) || j == n - 1){
                    if( j == n-1 && points[0].slopeTo( points[base] ) == points[0].slopeTo( points[j] )) j++;
                    if( j - base < 3) {
                        //slope changes, so move base to j.
                        base = j;
                        //System.out.println(base);
                        continue;
                    }
                    
                    Point[] pl = new Point[j-base+1];
                    for(int k = base; k < j; k++)
                    {
                        pl[k-base] = points[k];
                        //System.out.print(points[k] + " ");
                    }
                    pl[j-base] = points[0];
                    //System.out.println(points[0] + " ");

                    Arrays.sort(pl);

                    //System.out.println(pl[0] + " " + pl[j-base]);
                    LineSegment line = new LineSegment(pl[0],pl[j-base]);
                    if(pl[0] == points[0]){
                        lineSegments.push(line);
                    }
                    base = j;
                }
            }
        }
    }     
    public int numberOfSegments(){
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}