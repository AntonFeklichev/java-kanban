package manager.history;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> history = new CustomLinkedList<>();
    private Map<Integer, Node<Task>> nodesMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodesMap.containsKey(task.getId())) {
                remove(task.getId());
            }
            history.linkLast(task);
            nodesMap.put(task.getId(), history.tail);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getElements();
    }

    @Override
    public void remove(int id) {
        if (nodesMap.containsKey(id)) {
            Node<Task> nodeToRemove = nodesMap.get(id);
            history.removeNode(nodeToRemove);
        }
    }

    @Override
    public void removeAll() {
        history.clear();
    }

    private static class CustomLinkedList<E> {
        private Node<E> head;
        private Node<E> tail;
        private int size = 0;

        public void linkLast(E newElement) {
            final Node<E> oldTail = tail;
            final Node<E> newNode = new Node<>(oldTail, newElement, null);
            tail = newNode;
            if (oldTail == null) head = newNode;
            else oldTail.setNext(newNode);
            size++;
        }

        public List<E> getElements() {
            List<E> elements = new ArrayList<>(size);
            Node<E> currentNode = head;
            while (currentNode != null) {
                elements.add(currentNode.getData());
                currentNode = currentNode.getNext();
            }
            return elements;
        }

        public void removeNode(Node<E> nodeToRemove) {
            if (nodeToRemove != null) {
                final Node<E> next = nodeToRemove.getNext();
                final Node<E> prev = nodeToRemove.getPrev();

                if (next == null && prev == null){
                    head = null;
                    tail = null;
                    return;
                }

                if (prev == null) {
                    head = next;
                } else {
                    prev.setNext(next);
                }

                if (next == null) {
                    prev.setNext(null);
                    tail = prev;
                } else {
                    next.setPrev(prev);
                }

                size--;
            }
        }
        public void clear(){
            Node<E> head = this.head;
            while (head != null){
                removeNode(head);
                head = this.head;
            }
        }
    }
}
