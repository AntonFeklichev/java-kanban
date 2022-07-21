package manager;

import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.inMemory.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
