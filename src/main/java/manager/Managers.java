package manager;

import manager.file_backed.FileBackedTaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.http.HttpTaskManager;
import manager.in_memory.InMemoryTaskManager;

public class Managers {
    public static TaskManager getInMemory() {
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
