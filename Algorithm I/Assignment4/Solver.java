import java.util.Iterator;
import java.util.ArrayDeque;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int moves = -1;
    private ArrayDeque<Board> path;

    private class SearchNode implements Comparable<SearchNode> {
        public final Board board;
        public final int moves;
        public final SearchNode prev;
        public final int priority; 

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves; 
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if(initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));

        MinPQ<SearchNode> pq_inv = new MinPQ<>();
        pq_inv.insert(new SearchNode(initial.twin(), 0, null));

        while( !pq.min().board.isGoal() && !pq_inv.min().board.isGoal() ){

            SearchNode node = pq.delMin();
            for(Board b : node.board.neighbors()){
                if( !equalToPrev(b, node.prev) ) pq.insert(new SearchNode(b, node.moves+1, node)); 
            }
            //StdOut.println(node.board.toString());
            //StdOut.println(node.board.manhattan() + " " + node.moves);
            //StdOut.println(node_inv.board.toString());

            SearchNode node_inv = pq_inv.delMin();
            for(Board b : node_inv.board.neighbors()){
                if( !equalToPrev(b, node_inv.prev) ) pq_inv.insert(new SearchNode(b, node_inv.moves+1, node_inv)); 
            }

        }

        if(pq.min().board.isGoal()){
            path = new ArrayDeque<Board>();
            SearchNode node = pq.min();
            moves = node.moves;
            while(node != null){
                path.push(node.board);
                node = node.prev;
            }
        }
    }

    private boolean equalToPrev(Board b ,SearchNode node){
        while(node != null){
            if(node.board.equals(b)) return true;
            node = node.prev;
        }
        return false;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return moves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        if(moves == -1) return null;
        return path;
    }

    // test client (see below) 
    public static void main(String[] args){
            // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}