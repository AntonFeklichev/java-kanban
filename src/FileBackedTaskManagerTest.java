import manager.FileBackedTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.nio.file.Path;

public class FileBackedTaskManagerTest {
    public static void main(String[] args) {

        System.out.println("Saving to file test:");

        FileBackedTaskManager manager1 = new FileBackedTaskManager();

        Task homeworkTask = new Task("do homework", "do math", Status.NEW);
        Task carbonaraTask = new Task("go to the grocery", "buy spaghetti and bacon", Status.NEW);
        manager1.addTask(homeworkTask);
        manager1.addTask(carbonaraTask);

        Epic docEpic = new Epic("take care about health", "visit doctors", Status.NEW);
        Epic workEpic = new Epic("find clients", "", Status.NEW);
        manager1.addEpic(docEpic);
        manager1.addEpic(workEpic);
        Subtask docTask1 = new Subtask("go to surgeon", "get a surgeon consultation", Status.NEW, docEpic);
        Subtask docTask2 = new Subtask("go to dermatologist", "get a dermatologist consultation", Status.NEW, docEpic);
        Subtask workTask1 = new Subtask("find clients", "use profi.ru", Status.NEW, workEpic);
        manager1.addSubtask(docTask1);
        manager1.addSubtask(docTask2);
        manager1.addSubtask(workTask1);

        System.out.println("Filling history:");
        System.out.println(manager1.getEpicById(4));
        for (int i = 0; i < 4; i++) {
            System.out.println(manager1.getTaskById(1));
        }
        System.out.println(manager1.getSubtaskById(5));
        System.out.println(manager1.getTaskById(1));
        System.out.println("__________________________________________________________________");
        System.out.println("All tasks of manager 1:");
        System.out.println(manager1.getTasks());
        System.out.println("All epics of manager 1:");
        System.out.println(manager1.getEpics());
        System.out.println("All subtasks of manager 1:");
        System.out.println(manager1.getSubtasks());
        System.out.println("History of manager 1:");
        System.out.println(manager1.getHistory());
        System.out.println("\n__________________________________________________________________\n");
        System.out.println("Loading from file test:");
        FileBackedTaskManager manager2 = new FileBackedTaskManager();
        System.out.println("All tasks of manager 2 before loading:");
        System.out.println(manager2.getTasks());
        System.out.println("All epics of manager 2 before loading:");
        System.out.println(manager2.getEpics());
        System.out.println("All subtasks of manager 2 before loading:");
        System.out.println(manager2.getSubtasks());
        System.out.println("History of manager 2 before loading:");
        System.out.println(manager2.getHistory());
        manager2.loadTasksFromFile(Path.of("tasks.json"));
        manager2.loadEpicsFromFile(Path.of("epics.json"));
        manager2.loadSubtasksFromFile(Path.of("subtasks.json"));
        manager2.loadHistoryFromFile(Path.of("history.json"));
        System.out.println("All tasks of manager 2 after loading:");
        System.out.println(manager2.getTasks());
        System.out.println("All epics of manager 2 after loading:");
        System.out.println(manager2.getEpics());
        System.out.println("All subtasks of manager 2 after loading:");
        System.out.println(manager2.getSubtasks());
        System.out.println("History of manager 2 after loading:");
        System.out.println(manager2.getHistory());
    }
}
