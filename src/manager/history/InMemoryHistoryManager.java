package manager.history;

import tasks.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> history = new CustomLinkedList<>();
    private Map<Integer, CustomLinkedList.Node> nodesMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodesMap.containsKey(task.getId())) {
                remove(task.getId());
            }
            history.linkLast(task);
            System.out.println("added to history " + task);
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
            CustomLinkedList.Node nodeToRemove = nodesMap.get(id);
            history.removeNode(nodeToRemove);
            System.out.println("removed " + id + ": " + nodeToRemove.data + " from the history");

        }
    }
}
