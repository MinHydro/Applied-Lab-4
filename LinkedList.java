/**
 * Defines a doubly-linked list class
 * @author Minh Long Hang
 */
import java.util.NoSuchElementException;

public class LinkedList<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    /**** CONSTRUCTORS ****/

    /**
     * Instantiates a new LinkedList with default values
     * @postcondition A new empty LinkedList is created
     */
    public LinkedList() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Converts the given array into a LinkedList
     * @param array the array of values to insert into this LinkedList
     * @postcondition The array elements are added to the LinkedList in order
     */
    public LinkedList(T[] array) {
        this();
        if (array == null) {
            return; // The LinkedList remains empty
        }
    
        // Add each element from the array to the LinkedList
        for (T element : array) {
            addLast(element); // Adds the element to the end of the list
        }
    }

    /**
     * Instantiates a new LinkedList by copying another List
     * @param original the LinkedList to copy
     * @postcondition a new List object, which is an identical,
     * but separate, copy of the LinkedList original
     */
    public LinkedList(LinkedList<T> original) {
        this();
        if(original != null && original.length != 0){
            Node temp = original.first;
            while (temp != null){
                addLast(temp.data);
                temp = temp.next;

            }
            iterator = null;
        }
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     * @precondition LinkedList must not be empty
     * @return the value stored at node first
     * @throws NoSuchElementException if the LinkedList is empty
     */
    public T getFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("The LinkedList is empty.");
        }
        return first.data;
    }

    /**
     * Returns the value stored in the last node
     * @precondition LinkedList must not be empty
     * @return the value stored in the node last
     * @throws NoSuchElementException if the LinkedList is empty
     */
    public T getLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("The LinkedList is empty.");
        }
        return last.data;
    }

    /**
     * Returns the data stored in the iterator node
     * @precondition Iterator must not be null
     * @return the data stored in the iterator node
     * @throws NullPointerException if the iterator is null
     */
    public T getIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("Iterator is off end.");
        }
        return iterator.data;
    }

    /**
     * Returns the current length of the LinkedList
     * @return the length of the LinkedList from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the LinkedList is currently empty
     * @return whether the LinkedList is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns whether the iterator is offEnd, i.e. null
     * @return whether the iterator is null
     */
    public boolean offEnd() {
        return iterator == null;
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     * @param data the data to insert at the front of the LinkedList
     * @postcondition A new node is added to the front of the LinkedList
     */
    public void addFirst(T data) {
        Node newNode = new Node(data);
        if (length == 0) {
            first = newNode;
            last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        length++;
    }

    /**
     * Creates a new last element
     * @param data the data to insert at the end of the LinkedList
     * @postcondition A new node is added to the end of the LinkedList
     */
    public void addLast(T data) {
        Node newNode = new Node(data);
        if (length == 0) {
            first = newNode;
            last = newNode;
        } else {
            newNode.prev = last;
            last.next = newNode;
            last = newNode;
        }
        length++;
    }

    /**
     * removes the element at the front of the LinkedList
     * @precondition LinkedList must not be empty
     * @postcondition The first element is removed from the LinkedList
     * @throws NoSuchElementException if the LinkedList is empty
     */
    public void removeFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("The LinkedList is empty.");
        }
        if (length == 1) {
            first = null;
            last = null;
            iterator = null;
            length = 0;
        } else {
            if (iterator == first) {
                iterator = null;
            }
            first = first.next;
            first.prev = null;
            length--;
        }
    }

    /**
     * removes the element at the end of the LinkedList
     * @precondition LinkedList must not be empty
     * @postcondition The last element is removed from the LinkedList
     * @throws NoSuchElementException if the LinkedList is empty
     */
    public void removeLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("The LinkedList is empty.");
        }
        if (length == 1) {
            first = null;
            last = null;
            iterator = null;
            length = 0;
        } else {
            if (iterator == last) {
                iterator = null;
            }
            last = last.prev;
            last.next = null;
            length--;
        }
    }

    /**
     * Inserts an element after the iterator
     * @param data the data to insert after the iterator
     * @postcondition A new node is added after the iterator
     * @throws NullPointerException if the iterator is off end
     */
    public void addIterator(T data) throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("Iterator is off end.");
        }

        Node newNode = new Node(data);

        if (iterator == last) {
            addLast(data);
        } else {
            newNode.next = iterator.next;
            newNode.prev = iterator;
            iterator.next.prev = newNode;
            iterator.next = newNode;
            length++;
        }
    }

    /**
     * Removes the element the iterator is referencing and sets iterator to null
     * @precondition Iterator must not be off end
     * @postcondition The iterator is set to null and the node is removed
     * @throws NullPointerException if the iterator is off end
     */
    public void removeIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("Iterator is off end.");
        }

        if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            iterator.prev.next = iterator.next;
            iterator.next.prev = iterator.prev;
            length--;
        }

        iterator = null;
    }

    /**** ITERATOR OPERATIONS ****/

    /**
     * Positions the iterator at the start of the list
     * @postcondition Iterator is set to the first element
     */
    public void positionIterator() {
        iterator = first;
    }

    /**
     * Moves the iterator one position forward in the list
     * @precondition Iterator must not be null
     * @postcondition Iterator moves one step forward
     * @throws NullPointerException if the iterator is off end
     */
    public void advanceIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("Iterator is off end.");
        }
        iterator = iterator.next;
    }

    /**
     * Moves the iterator one position back in the list
     * @precondition Iterator must not be null
     * @postcondition Iterator moves one step backward
     * @throws NullPointerException if the iterator is off end
     */
    public void reverseIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("Iterator is off end.");
        }
        iterator = iterator.prev;
    }

    /**** ADDITIONAL OPERATIONS ****/

     /**
     * Re-sets LinkedList to empty as if the
     * default constructor had just been called
     */
    public void clear() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Converts the LinkedList to a String, with each value separated by a blank
     * line At t he end of the String, place a new line character
     * @return the LinkedList as a String
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Node temp = first;
        while (temp != null) {
            result.append(temp.data + " ");
            temp = temp.next;
        }
        return result.toString() + "\n";
    }
    /**
     * Determines whether the given Object is
     * another LinkedList, containing
     * the same data in the same order
     * @param obj another Object
     * @return whether there is equality
     */
    @SuppressWarnings("unchecked") //good practice to remove warning here
    @Override public boolean equals(Object obj) {
        if(obj == this){
            return true;
          } 
        else if(!(obj instanceof LinkedList)){
            return false;
          } 
        else {
            LinkedList<T> At = (LinkedList<T>) obj;
            if (length != At.length){
                return false;
            }
            else {
                Node temp1 = this.first;
                Node temp2 = At.first;
                while(temp1 != null){
                    if(temp1.data == null || temp2.data == null){
                        if (temp1.data != temp2.data){
                            return false;
                        }
                    }
                        else if(!(temp1.data.equals(temp2.data))){
                        return false;
                    }
                    temp1 = temp1.next;
                    temp2 = temp2.next;
                }
                return true;
              }
          }
          
    }
     /**CHALLENGE METHODS*/

   /**
     * Moves all nodes in the list towards the end
     * of the list the number of times specified
     * Any node that falls off the end of the list as it
     * moves forward will be placed the front of the list
     * For example: [1, 2, 3, 4, 5], numMoves = 2 -> [4, 5, 1, 2 ,3]
     * For example: [1, 2, 3, 4, 5], numMoves = 4 -> [2, 3, 4, 5, 1]
     * For example: [1, 2, 3, 4, 5], numMoves = 7 -> [4, 5, 1, 2 ,3]
     * @param numMoves the number of times to move each node.
     * @precondition numMoves >= 0
     * @postcondition iterator position unchanged (i.e. still referencing
     * the same node in the list, regardless of new location of Node)
     * @throws IllegalArgumentException when numMoves < 0
     */
    public void spinList(int numMoves) throws IllegalArgumentException {
        if (numMoves < 0) {
            throw new IllegalArgumentException("numMoves cannot be negative.");
        }
    
        // No need to spin if list is empty or only has 1 element
        if (length <= 1 || numMoves == 0) {
            return;
        }
    
        // Reduce numMoves by modulo to avoid unnecessary rotations
        numMoves = numMoves % length;
    
        // If numMoves is 0 after the modulo operation, no need to spin
        if (numMoves == 0) {
            return;
        }
    
        // Find the new last node after spinning
        Node newLast = first;
        for (int i = 1; i < length - numMoves; i++) {
            newLast = newLast.next;
        }
    
        // The node after newLast becomes the new first node
        Node newFirst = newLast.next;
    
        // Break the link between newLast and newFirst
        newLast.next = null;
        newFirst.prev = null;
    
        // Update the links: the old last node should now point to the old first
        last.next = first;
        first.prev = last;
    
        // Update first and last pointers
        first = newFirst;
        last = newLast;
    }

 /**
     * Splices together two LinkedLists to create a third List
     * which contains alternating values from this list
     * and the given parameter
     * For example: [1,2,3] and [4,5,6] -> [1,4,2,5,3,6]
     * For example: [1, 2, 3, 4] and [5, 6] -> [1, 5, 2, 6, 3, 4]
     * For example: [1, 2] and [3, 4, 5, 6] -> [1, 3, 2, 4, 5, 6]
     * @param list the second LinkedList
     * @return a new LinkedList, which is the result of
     * alternating this and list
     * @postcondition this and list are unchanged
     */
    public LinkedList<T> altLists(LinkedList<T> list) {
        LinkedList<T> result = new LinkedList<>();
        if (list == null) {
            return new LinkedList<>(this);
        }
        Node thisCurrent = this.first;
        Node listCurrent = list.first;
        while (thisCurrent != null || listCurrent != null) {
            if (thisCurrent != null) {
                result.addLast(thisCurrent.data);
                thisCurrent = thisCurrent.next;
            }
            if (listCurrent != null) {
                result.addLast(listCurrent.data);
                listCurrent = listCurrent.next;
            }
        }
        return result;
    }
     /** MORE METHODS */
    /**
     * Returns each element in the LinkedList along with its
     * numerical position from 1 to n, followed by a newline.
     * @return the numbered LinkedList elements as a String.
     */
    public String numberedListString() {
        if (length == 0) {
            return "\n";
        }
        
        StringBuilder result = new StringBuilder();
        Node current = first;
        int position = 1;
        
        while (current != null) {
            result.append(position).append(". ")
                  .append(current.data).append("\n");
            current = current.next;
            position++;
        }
        
        return result.append("\n").toString();
    }

    /**
     * Searches the LinkedList for a given element's index.
     * @param data the data whose index to locate.
     * @return the index of the data or -1 if the data is not contained
     * in the LinkedList.
     */
    public int findIndex(T data) {
        if (length == 0) {
            return -1;
        }
        
        Node current = first;
        int index = 0;
        
        while (current != null) {
            if ((data == null && current.data == null) || 
                (data != null && data.equals(current.data))) {
                return index;
            }
            current = current.next;
            index++;
        }
        
        return -1;
    }

    /**
     * Advances the iterator to location within the LinkedList
     * specified by the given index.
     * @param index the index at which to place the iterator.
     * @precondition index >= 0, index < length
     * @throws IndexOutOfBoundsException when the index is out of bounds
     */
    public void advanceIteratorToIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
    
        positionIterator(); // Start from the beginning
        
        for (int i = 0; i < index; i++) {
            advanceIterator();
        }
    }
}
    




