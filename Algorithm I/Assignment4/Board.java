import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int sz;
    private Stack<int[]> neighbourDir;
    private int zeroX,zeroY;
    private int[][] board;
    private int hamming,manhattan;
    
    private static final int[] up = {-1,0},down = {1,0},left = {0,-1} , right = {0,1};
    
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        sz = tiles.length;
        board = new int[sz][sz];
        neighbourDir = new Stack<int[]>();

        for(int i = 0; i < sz; i++){
            for(int j = 0; j < sz; j++){
                board[i][j] = tiles[i][j];
                if(tiles[i][j] == 0){
                    if( i == 0 || i == sz-1){
                        neighbourDir.push(i == 0 ? down : up);
                    }
                    else {
                        neighbourDir.push(up);
                        neighbourDir.push(down);
                    }
                    if( j == 0 || j == sz - 1){
                        neighbourDir.push( j == 0 ? right : left);
                    }
                    else {
                        neighbourDir.push(left);
                        neighbourDir.push(right);
                    }
                    zeroX = i;
                    zeroY = j;
                }
            }
        }

        hamming = calculateHamming();
        manhattan = calculateManhattan();
        //StdOut.println("neighbour count:" + neighbourDir.size());
        // for(int[] d : neighbourDir){
        //     StdOut.println(d[0] + " " + d[1]);
        // }
    }
                                           
    // string representation of this board
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(Integer.toString(sz) + "\n");
        for(int i = 0; i < sz; i++){
            for(int j = 0; j < sz; j++){
                str.append(Integer.toString(board[i][j]) + " ");
            }
            str.append("\n");
        }        
        return str.toString();
    }

    // board dimension n
    public int dimension(){ return sz;}

    // number of tiles out of place
    public int hamming(){
        return hamming;
    }

    private int calculateHamming(){
        int count = 0;
        for(int i = 0; i < sz; i++){
            for(int j = 0; j < sz; j++){
                if(board[i][j] != 0 && sz*i+j+1 != board[i][j]) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        return manhattan;
    }

    private int calculateManhattan(){
        int dis = 0;
        int num = 0;
        for(int i = 0; i < sz; i++){
            for(int j = 0; j < sz; j++){
                num = board[i][j];
                if(num == 0){ continue;
                    //dis += i;
                    //dis += j;
                    //StdOut.println("manh dis of " + num + "is " + (i + j));
                }
                else{
                    dis += Math.abs( (num - 1) / sz - i );
                    dis += Math.abs( (num - 1) % sz - j );
                    //StdOut.println("manh dis of" + num + "is " + ( Math.abs((num - 1) / sz - i) + Math.abs( (num - 1) % sz - j ) ));
                }

            }
        }
        return dis;
    }

    // is this board the goal board?
    public boolean isGoal(){ return hamming() == 0;}

    // does this board equal y?
    public boolean equals(Object y){
        if(y == null || getClass() != y .getClass() || sz != ((Board)y).dimension() ) return false;
        return Arrays.deepEquals( ((Board)y).board,board );
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator()
                {
                    return new Iterator<Board>(){
                        Iterator<int[]> it = neighbourDir.iterator();

                        @Override
                        public boolean hasNext(){return it.hasNext();}

                        @Override
                        public Board next(){
                            int[] dir = it.next();
                            int swX = zeroX + dir[0];
                            int swY = zeroY + dir[1];

                            int tmp = board[zeroX][zeroY];
                            board[zeroX][zeroY] = board[swX][swY];
                            board[swX][swY] = tmp;

                            Board b = new Board(board);

                            tmp = board[zeroX][zeroY];
                            board[zeroX][zeroY] = board[swX][swY];
                            board[swX][swY] = tmp;

                            return b;
                        }
                };
                
            }    
        };
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        int x1 = 0 , y1 = 0;
        if(zeroX == 0 && zeroY == 0) y1 = 1;
        for(int x2 = 0; x2 < sz ; x2++){
            for(int y2 = 0 ; y2 < sz ; y2++){
                if( (x2 != x1 && x2 != zeroX) || (y2 != y1 && y2 != zeroY)){
                    int tmp = board[x1][y1];
                    board[x1][y1] = board[x2][y2];
                    board[x2][y2] = tmp;

                    Board b = new Board(board);

                    tmp = board[x1][y1];
                    board[x1][y1] = board[x2][y2];
                    board[x2][y2] = tmp;

                    return b;
                }
            }
        }
        throw new IllegalStateException();
    }

    // unit testing (not graded)
    public static void main(String[] args){
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Board b1 = new Board(tiles);
        StdOut.println(initial.equals(b1));

        StdOut.println(initial.toString());
        StdOut.println(initial.twin().toString());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        for(Board b : initial.neighbors()){
            StdOut.println(b.toString());
            // StdOut.println(b.equals(b1));
            // for(Board b2 : b.neighbors()){
            //     StdOut.println(b2.toString());
            //     StdOut.println(b2.equals(initial));
            // }
        }
    }
}