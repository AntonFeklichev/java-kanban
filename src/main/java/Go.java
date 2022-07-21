import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonTypeAdapters.ZonedDateTimeAdapter;
import manager.fileBacked.GsonFileBackedTaskManager;
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
        GsonFileBackedTaskManager manager = new GsonFileBackedTaskManager("save");
        manager.removeAllTasks();
        Task task = new Task("task", "desc of task", Status.NEW, 1);
        Task anotherTask = new Task(ZonedDateTime.now(), 10);
        manager.addTask(task);
        manager.addTask(anotherTask);
        manager.getTaskById(1);
        Epic epic = new Epic();
        manager.addEpic(epic);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getTaskById(1);
    }
}
