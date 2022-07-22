import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonTypeAdapters.ZonedDateTimeAdapter;
import manager.fileBacked.FileBackedTaskManager;
import manager.http.HttpTaskServer;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.time.ZonedDateTime;

public class Go {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        FileBackedTaskManager manager = new FileBackedTaskManager("save");
        manager.removeAllTasks();
        Task task = new Task("task", "desc of task", Status.NEW, 1);
        Task anotherTask = new Task(ZonedDateTime.now(), 10);
        manager.addTask(task);
        manager.addTask(anotherTask);
        manager.getTaskById(1);
        Epic epic = new Epic();
        manager.addEpic(epic);
        HttpTaskServer server = new HttpTaskServer(8080, 0);
        server.start();
    }
}
