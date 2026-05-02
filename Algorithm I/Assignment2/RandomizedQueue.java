import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int r;
    private Item[] que;
    private int capacity;
    private int size;
    // construct an empty deque
    public RandomizedQueue()
    {
        r = size = 0;
        capacity = 8;
        que = (Item[]) new Object[capacity];
    }

    // is the deque empty?
    public boolean isEmpty(){ return size==0;}

    // return the number of items on the randomized queue
    public int size(){return size;}

    // add the item
    public void enqueue(Item item){
        if(item == null) throw new IllegalArgumentException();
        que[r++] = item; size++;
        if( size == capacity) expand();
    }

    // remove and return a random item
    public Item dequeue(){
        if(isEmpty()) throw new java.util.NoSuchElementException();
        int rand = StdRandom.uniformInt(size);
        // for(Item it : que){
        //     System.out.println(it + " ");
        // }
        Item temp = que[--r];
        que[r] = que[rand];
        que[rand] = temp;
        Item re = que[r];
        que[r] = null;
        if(--size < capacity / 4) shrink();
        return re;
    }
    //return a random item, but do not remove it.
    public Item sample(){
        if(isEmpty()) throw new java.util.NoSuchElementException();
        int rand = StdRandom.uniformInt(size);
        return que[rand];
    }
    // return an iterator over items in random order
    public Iterator<Item> iterator(){
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item>{
        private int current = 0;
        private int[] ReadSequence;
        public RandomizedQueueIterator(){
            ReadSequence = new int[size];
            for(int i = 0;i < size ; i++) ReadSequence[i] = i;
            StdRandom.shuffle(ReadSequence);
        }

        public boolean hasNext() {return current != size;}
        public void remove() {throw new UnsupportedOperationException();}
        public Item next(){
            if(size == 0) throw new NoSuchElementException();
            Item item;
            item = que[ReadSequence[current++]];
            return item;
            
        }
    }

    private void expand(){
        Item[] newQue = (Item[]) new Object[capacity*2];
        int i = -1;
        while( ++i < capacity) newQue[i] = que[i];
        que = newQue;
        capacity *= 2;
        // System.out.println("Expanded:");
        // for(Item it : que){
        //     System.out.println(it + " ");
        // }
    }

    private void shrink(){
        Item[] newQue = (Item[]) new Object[capacity/2];
        int i = -1;
        while(que[++i] != null) newQue[i] = que[i];
        que = newQue;
        capacity /= 2;
        // System.out.println("Shrinked:");
        // for(Item it : que){
        //     System.out.println(it + " ");
        // }
    }
    // unit testing (required)
    public static void main(String[] args)
    {
        String[] words = new String[]{"this","is","a","test","of","random","queue"};
        RandomizedQueue<String> strQue = new RandomizedQueue<>();
        for(String w : words){
            strQue.enqueue(w);
        }
        System.out.println("RandomQueue Size:" + strQue.size());
        System.out.println("This is the first iterator:");
        for(String w : strQue){
            System.out.print(w+" ");
        }
        System.out.println("\nThis is the second iterator:");
        for(String w : strQue){
            System.out.print(w+" ");
        }
        System.out.println("\nSample:"+strQue.sample());
        int s = strQue.size();
        for(int i = 0 ; i < s ; i++) System.out.println("Remove Random Item "+i+":"+strQue.dequeue());
        System.out.println("RandomQueue is Empty:" + strQue.isEmpty() + ",RandomQueue size: " + strQue.size());
    }

}