package manager.history;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> history = new CustomLinkedList<>();
    private Map<Integer, Node> nodesMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodesMap.containsKey(task.getId())) {
                remove(task.getId());
            }
            history.linkLast(task);
            nodesMap.put(task.getId(), history.getTail());
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

    private static class CustomLinkedList<E> {
        private Node<E> head;
        private Node<E> tail;
        private int size = 0;

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

        public Node<E> getTail() {
            return tail;
        }
    }
}
