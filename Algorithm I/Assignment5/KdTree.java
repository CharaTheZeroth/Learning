import java.util.Stack;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class KdTree {
    
    //private static final boolean RED = true, BLACK = false; //DO NOT USE RB-TREE
    private class PTreeNode{
        private final Point2D point;
        //private final RectHV rect;
        private final int level;        
        private PTreeNode left,right;
        //private boolean color;
        public PTreeNode(Point2D point, int level){
            this.point = point;
            this.level = level;
            //this.color = color;
            //this.rect = rect;
        }
        public int comparePoint(Point2D p){
            if(isHorizontal()) return Double.compare(p.x(), x());
            else return Double.compare(p.y(), y());
        }
        public boolean isHorizontal(){
            return level % 2 == 0;
        }
        public double x(){return point.x();}
        public double y(){return point.y();}
    }

    private int size;
    private PTreeNode root;

    public KdTree(){
        size = 0;
    }                               // construct an empty set of points 
    public boolean isEmpty(){
        return size == 0;
    }                      // is the set empty? 
    public int size(){
        return size;
    }                         // number of points in the set 
    public void insert(Point2D p){
        if( p == null ) throw new IllegalArgumentException();
        
        root = createNode(root, p, 0);
    }              // add the point to the set (if it is not already in the set)

    private PTreeNode createNode(PTreeNode node,Point2D point, int level){
        if( node == null ) { size++; return new PTreeNode(point, level); }
        
        int cmp = node.comparePoint(point);
        if( cmp < 0 ) node.left = createNode(node.left, point, level+1);
        else if( cmp > 0 )node.right = createNode(node.right, point, level+1);
        else if (node.point.equals(point)) return node;
        else node.left = createNode(node.left, point, level+1);
        // if( isRed(node.right) && !isRed(node.right) ) node = rotateLeft(node);
        // if( node.left != null && isRed(node.left) && isRed(node.left.left) ) node = rotateRight(node);
        // if( isRed(node.left) && isRed(node.right) ) filpColor(node);
        return node;
    }

    // private boolean isRed(PTreeNode node){
    //     return node != null && node.color;
    // }

    // private PTreeNode rotateLeft(PTreeNode h){
    //     PTreeNode x = h.right;
    //     h.right = x.left;
    //     x.left = h;
    //     x.color = h.color;
    //     h.color = RED;
    //     return x;
    // }

    // private PTreeNode rotateRight(PTreeNode h){
    //     PTreeNode x = h.left;
    //     h.left = x.right;
    //     x.right = h;
    //     x.color = h.color;
    //     h.color = RED;
    //     return x;
    // }

    // private void filpColor(PTreeNode node){
    //     node.color = RED;
    //     node.left.color = BLACK;
    //     node.right.color = BLACK;
    // }

    public boolean contains(Point2D p){
        if( p == null ) throw new IllegalArgumentException();
        return find( root, p ) != null;
    }            // does the set contain point p? 

    private PTreeNode find(PTreeNode node,Point2D point){
        if( node == null ) return null;

        int cmp = node.comparePoint(point);
        if( cmp < 0 ) return find(node.left, point);
        else if ( cmp > 0 ) return find(node.right, point);
        else{
            if( node.point.equals(point) ) return node;
            else return find (node.left, point);
        }
    }

    public void draw(){
        StdDraw.enableDoubleBuffering();
        //StdDraw.setXscale(0,1);
        //StdDraw.setXscale(0,1);
        DrawNode(root);
        StdDraw.show();
    }                         // draw all points to standard draw 

    private void DrawNode(PTreeNode node){
        if(node == null) return;
        StdDraw.point(node.x(), node.y());
        DrawNode(node.left);
        DrawNode(node.right);
    }

    public Iterable<Point2D> range(RectHV rect){
        if ( rect == null ) throw new IllegalArgumentException();
        Stack<Point2D> s = new Stack<>();
        intersectRectHV(root, rect, s);
        return s;
    }             // all points that are inside the rectangle (or on the boundary) 

    private void intersectRectHV(PTreeNode node, RectHV rect, Stack<Point2D> s){
        if(node == null) return;

        if(rect.contains(node.point)) s.push(node.point);
        int cmp = rectSide(node, rect);
        if(cmp < 0) intersectRectHV(node.left, rect, s);
        else if(cmp > 0) intersectRectHV(node.right, rect, s);
        else{
            intersectRectHV(node.left, rect, s);
            intersectRectHV(node.right, rect, s);
        }
    }

    private int rectSide(PTreeNode node, RectHV rect){
        if(node.isHorizontal()){
            if(rect.xmax() < node.x()) return -1;
            if(rect.xmin() > node.x()) return 1;
            return 0; 
        }
        else{
            if(rect.ymax() < node.y()) return -1;
            if(rect.ymin() > node.y()) return 1;
            return 0;             
        }
    }

    public Point2D nearest(Point2D p){
        if( p == null ) throw new IllegalArgumentException();
        nData result = searchNearest(root, p, null);
        return result == null ? null : result.node.point;
    }             // a nearest neighbor in the set to point p; null if the set is empty 

    private nData searchNearest(PTreeNode node, Point2D point, nData fatherBest){
        if(node == null) return null;
        //System.out.println("Searching " + node.point);
        int cmp = node.comparePoint(point);
        PTreeNode firstBranch = (cmp <= 0) ? node.left : node.right;
        PTreeNode secondBranch = (cmp <= 0) ? node.right : node.left;

        nData current = new nData(node,node.point.distanceSquaredTo(point));
        nData best = searchNearest(firstBranch, point, current);
    
        if (best == null || current.distanceSquared < best.distanceSquared) {
            best = current;
        }
    
        if(fatherBest != null && fatherBest.distanceSquared < best.distanceSquared){
            best = fatherBest;
        }
    
        if (secondBranch != null) {
            double axisDist = Math.abs(node.isHorizontal() 
                ? node.x() - point.x() 
                : node.y() - point.y());
            //System.out.println("axisDist " + axisDist * axisDist);
            //System.out.println("best " + best.distanceSquared);
            if (axisDist * axisDist < best.distanceSquared) {
                nData otherBest = searchNearest(secondBranch, point, best);
                if (otherBest != null && otherBest.distanceSquared < best.distanceSquared) {
                    best = otherBest;
                }
            }
        }
    
        return best;
    }
    private class nData
    {
        public final PTreeNode node; 
        public final double distanceSquared;
        public nData(PTreeNode node, double disSquared){
            this.node = node;
            distanceSquared = disSquared;
        }
    }
    public static void main(String[] args){
        In in = new In(args[0]);
        KdTree tree = new KdTree();
        while (!in.isEmpty()) {
            double a = in.readDouble();
            double b = in.readDouble();
            tree.insert( new Point2D(a,b) );
        }
        double c = StdRandom.uniformDouble();
        double d = StdRandom.uniformDouble();
        double e = StdRandom.uniformDouble();
        double f = StdRandom.uniformDouble();
        Point2D p = new Point2D(c,d);
        RectHV rect = new RectHV(Math.min(c,e),Math.min(d,f),Math.max(c,e),Math.max(d,f));
        System.out.println(p);
        System.out.println(tree.nearest(new Point2D(0.75,1.0)));
        System.out.println("Rect: " + rect);
        System.out.println("Rect Detection:");
        for(Point2D point : tree.range(rect)){
            System.out.println(point);
        }
        rect.draw();
        tree.draw();
    }                  // unit testing of the methods (optional)
}