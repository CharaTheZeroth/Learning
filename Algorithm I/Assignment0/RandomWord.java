import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String args[]){
        String survivor="";
        for(int i = 1;!StdIn.isEmpty();i++){
            String cur = StdIn.readString();
            if(StdRandom.bernoulli(1.f/i)) survivor = cur;
        }
        StdOut.println(survivor);
    }
}
