import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class Deque<Item> implements Iterable<Item> {
    private int l,r;
    private Item[] que;
    private int capacity;
    private int size;
    // construct an empty deque
    public Deque()
    {
        r = size = 0;
        capacity = 8;
        l = capacity - 1;
        que = (Item[]) new Object[capacity];
    }

    // is the deque empty?
    public boolean isEmpty(){ return size==0;}

    // return the number of items on the deque
    public int size(){return size;}

    // add the item to the front
    public void addFirst(Item item){
        if(item == null) throw new IllegalArgumentException();
        que[l] = item; size++;
        l = (l - 1 + capacity) % capacity;
        if(size == capacity) expand();
    }

    // add the item to the back
    public void addLast(Item item){
        if(item == null) throw new IllegalArgumentException();
        que[r] = item; size++;
        r = (r+1) % capacity;
        if(size == capacity) expand();
        // System.out.println("Added last: " + item);
        // for(Item it : que){
        //     System.out.println(it + " ");
        // }
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if(isEmpty()) throw new java.util.NoSuchElementException();
        l = (l + 1) % capacity;
        Item re = que[l];
        que[l] = null;
        if(--size < capacity / 4) shrink();
        // System.out.println("Removed first:");
        // for(Item it : que){
        //     System.out.println(it + " ");
        // }
        return re;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if(isEmpty()) throw new java.util.NoSuchElementException();
        r = (r - 1 + capacity) % capacity;
        Item re = que[r];
        que[r] = null;
        if(--size != 0 && size < capacity / 4) shrink();

        // System.out.println("Removed last: " + re);
        // for(Item it : que){
        //     System.out.println(it + " ");
        // }
        return re;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>{
        private int current = 0;

        public boolean hasNext() {return current != size;}
        public void remove() {throw new UnsupportedOperationException();}
        public Item next(){
            if(!hasNext()) throw new NoSuchElementException();
            Item item;
            int i = (l + current + 1) % capacity;
            current++;
            item = que[i];
            return item;
        }
    }

    private void expand(){
        Item[] newQue = (Item[]) new Object[capacity*2];
        int i = -1;
        while( ++i < capacity){
            l = (l + 1) % capacity;
            newQue[i] = que[l];
        }
        que = newQue;
        l = capacity*2-1;
        r = capacity;
        capacity *= 2;
    }

    private void shrink(){
        Item[] newQue = (Item[]) new Object[capacity/2];
        int i = -1;
        while(que[(l+1)%capacity] != null){
            l = (l+1)%capacity;
            newQue[++i] = que[l];
        }
        l = capacity/2-1;
        r = i+1;
        que = newQue;
        capacity /= 2;
    }
    // unit testing (required)
    public static void main(String[] args)
    {
        String[] words = new String[]{"this","is","a","test"};
        String[] wordsFirst = new String[]{"first","from","me","push"};
        Deque<String> strQue = new Deque<>();
        for(String w : wordsFirst){
            strQue.addFirst(w);
        }
        for(String w : words){
            strQue.addLast(w);
        }
        System.out.println("Deque Size:" + strQue.size());
        for(String w : strQue){
            System.out.print(w+" ");
        }
        System.out.println("\nRemove Front:"+strQue.removeFirst());
        System.out.println("Remove Last:"+strQue.removeLast());
        int s = strQue.size();
        for(int i = 0 ; i < s ; i++) System.out.println("Remove Front"+i+":"+strQue.removeFirst());
        System.out.println("Deque is Empty:" + strQue.isEmpty() + ",Deque size: " + strQue.size());
        // Deque<Integer> deque = new Deque<>();
        //  deque.addLast(1);
        //  deque.removeLast();      
        //  deque.addLast(3);
        //  deque.addLast(4);
        //  deque.removeLast();     
        //  deque.addLast(6);
        //  deque.addLast(7);
        //  deque.removeLast();     
        //  deque.removeLast();     
        //  deque.removeLast();      

    }

}