import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class PercolationStats {

    private int[] results;
    private int trial;
    private int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n <=0 || trials <=0) throw new IllegalArgumentException();
        this.n = n;
        trial = trials;
        int[] sequence = new int[n*n];
        for(int i = 0;i < n*n;i++) sequence[i] = i;
        results = new int[trials];
        for(int i = 0;i < trials;i++){
            StdRandom.shuffle(sequence);
            Percolation p = new Percolation(n);
            int j = 0;
            while(!p.percolates()){
                p.open(sequence[j] / n + 1, sequence[j] - (sequence[j] / n) * n + 1);
                j++;
            }
            results[i] = p.numberOfOpenSites();
        }
    }

    // sample mean of percolation threshold
    public double mean(){return StdStats.mean(results) / n / n;}

    // sample standard deviation of percolation threshold
    public double stddev(){return StdStats.stddev(results) / n / n;}

    // low endpoint of 95% confidence interval
    public double confidenceLo() {return (StdStats.mean(results) - 1.96 * StdStats.stddev(results) / Math.sqrt(trial)) / n / n;}

    // high endpoint of 95% confidence interval
    public double confidenceHi() {return (StdStats.mean(results) + 1.96 * StdStats.stddev(results) / Math.sqrt(trial)) / n / n;}

   // test client (see below)
   public static void main(String[] args)
   {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println(String.format("95%% confidence interval = [%f, %f]",stats.confidenceLo(),stats.confidenceHi()));
   }

}