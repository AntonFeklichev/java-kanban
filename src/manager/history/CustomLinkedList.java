package manager.history;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomLinkedList<E> {
    public class Node<E> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public void linkFirst(E element) {
        final Node<E> oldHead = head;
        final Node<E> newNode = new Node<>(null, element, oldHead);
        head = newNode;
        if (oldHead == null)
            tail = newNode;
        else
            oldHead.prev = newNode;
        size++;
    }

    public E getFirst() {
        final Node<E> curHead = head;
        if (curHead == null)
            throw new NoSuchElementException();
        return head.data;
    }

    public void linkLast(E e) {
        final Node<E> l = tail;
        final Node<E> newNode = new Node<>(l, e, null);
        tail = newNode;
        if (l == null)
            head = newNode;
        else
            l.next = newNode;
        size++;
    }

    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException();
        }
        return tail.data;
    }

    public int size() {
        return this.size;
    }

    public List<E> getElements() {
        List<E> elements = new ArrayList<>(size);
        Node<E> currentNode = head;
        if (currentNode == null) {
            System.out.println("Linked list is empty");
            return null;
        } else {
            while (currentNode != null) {
                elements.add(currentNode.data);
                currentNode = currentNode.next;
            }
        }
        return elements;
    }

    public void removeNode(Node<E> x) {
        if (x != null) {
            final Node<E> next = x.next;
            final Node<E> prev = x.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.data = null;
            size--;
        }
    }

    public Node getTail() {
        return tail;
    }
}