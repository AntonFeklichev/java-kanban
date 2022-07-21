package manager;

import manager.fileBacked.FileBackedTaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.inMemory.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBacked() {
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
