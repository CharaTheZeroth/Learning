import edu.princeton.cs.algs4.StdIn;

public class Permutation {
public static void main(String[] args){
        int outPutCount = Integer.parseInt(args[0]);
        RandomizedQueue<String> rQue = new RandomizedQueue<>();
        while(!StdIn.isEmpty()){
           rQue.enqueue(StdIn.readString());
        }
        for(int i = 0;i<outPutCount;i++){
            System.out.println( rQue.dequeue());
        }
    }
}