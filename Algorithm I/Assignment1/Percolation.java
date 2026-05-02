public class Percolation {

    private int openSiteCount;
    private int gridSize;
    private boolean[] grid;
    private FastUFPercolation uf;
    
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n <= 0) throw new IllegalArgumentException();

        gridSize = n;
        openSiteCount = 0;

        grid = new boolean[n*n+1];
        uf = new FastUFPercolation(n);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if(outOfBoundary(row,col)) throw new IllegalArgumentException();
        if(grid[getIdx(row, col)]) return;
        grid[getIdx(row, col)] = true;
        openSiteCount++;
        if(row == 1) uf.SetConnectionFlag(getIdx(row, col),1);
        if(row == gridSize) uf.SetConnectionFlag(getIdx(row, col),2);
        int[] rs = {row-1,row+1};
        int[] cs = {col-1,col+1};
        for (int i : cs ) {
            if(!outOfBoundary(row,i) && grid[getIdx(row, i)]) uf.Union(getIdx(row, col), getIdx(row, i));
        }
        for (int i : rs) {
            if(!outOfBoundary(i,col) && grid[getIdx(i,col)]) uf.Union(getIdx(row, col), getIdx(i,col));
       }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if(row < 1 || row > gridSize || col < 1 || col > gridSize) throw new IllegalArgumentException();
         return grid[getIdx(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if(row < 1 || row > gridSize || col < 1 || col > gridSize) throw new IllegalArgumentException();
        return uf.IsFull(getIdx(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites(){return openSiteCount;}

    // does the system percolate?
    public boolean percolates() {return uf.IsPercolate();}

    // test client (optional)
    public static void main(String[] args){    }

    //public int GetConnectionToTopAndBot(int row, int col) {return uf.GetConnectionToTopAndBot(getIdx(row, col));}

    private int getIdx(int row,int col){
        return (row-1) * gridSize + col;
    }
    private boolean outOfBoundary(int row,int col){
        return row < 1 || row > gridSize || col < 1 || col > gridSize ;
    }

    private class FastUFPercolation{
        private int[] id;
        private int[] sz;
        private int[] uAb;
        private boolean isPercolate = false;

        public FastUFPercolation(int n){
            id = new int[n*n+2];
            sz = new int[n*n+2];
            uAb = new int[n*n+2];
            for(int i = 0; i < n*n+2; i++)
                {
                    id[i]=i;
                    sz[i]=1;    
                }
        }

        private int root(int idx){
            while(id[idx]!=idx) 
                {
                    id[idx] = id[id[idx]];
                    idx = id[idx];
                }
            return idx;
        }

        public void Union(int a,int b){
            int rootA = root(a);
            int rootB = root(b);
            if(rootA == rootB) return;
            int symbol = uAb[rootA] | uAb[rootB];
            if(symbol == 3) isPercolate = true;
            if(sz[rootA] < sz[rootB]) 
                {
                    id[rootA] = rootB; sz[rootB] += sz[rootA];
                    uAb[rootB] = symbol;
                }
            else {id[rootB] = rootA; sz[rootA]+= sz[rootB]; uAb[rootA] = symbol;}
        }

        //public boolean Find(int a,int b) {return root(a) == root(b);}

        public boolean IsFull(int idx) {return (uAb[root(idx)] & 1) == 1;}

        public void SetConnectionFlag(int idx , int flag)
        {
            if(flag != 1 && flag != 2) throw new IllegalArgumentException(); 
            uAb[idx] = uAb[idx] | flag;
            if(uAb[idx] == 3) isPercolate = true;
        }

        public boolean IsPercolate(){ return isPercolate;}
    }
}