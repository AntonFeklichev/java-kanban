package manager;

import manager.fileBacked.FileBackedTaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.http.HttpTaskManager;
import manager.inMemory.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBacked(String saveDir) {
        return new FileBackedTaskManager();
    }

    public static TaskManager getHttp(String url) {
        return new HttpTaskManager(url);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
