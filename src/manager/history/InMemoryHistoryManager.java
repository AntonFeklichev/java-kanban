package manager.history;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
//
    private final int MAX_HISTORY_SIZE = 10;
    private List<Task> history = new LinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() == MAX_HISTORY_SIZE) {
                history.remove(0);
            }
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
