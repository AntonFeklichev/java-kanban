package manager.file_backed;

import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private FileBackedTaskManager manager1;
    private FileBackedTaskManager manager2;
    private Task homeworkTask;
    private Task carbonaraTask;
    private Epic docEpic;
    private Epic workEpic;
    private Subtask docTask1;
    private Subtask docTask2;
    private Subtask workTask1;
    private ZonedDateTime now;
    private long defaultDuration;


    @Override
    public FileBackedTaskManager createManager() {
        return new FileBackedTaskManager("save");
    }

    @BeforeEach
    public void initManagerAndTime() {
        manager1 = createManager();
        now = ZonedDateTime.now();
        defaultDuration = 20;
    }

    public void initTasks() {
        homeworkTask = new Task("do homework", "do math", 1, Status.NEW, TaskTypes.TASK, now, defaultDuration, now.plusMinutes(defaultDuration));
        carbonaraTask = new Task("go to the grocery", "buy spaghetti and bacon", 2, Status.NEW, TaskTypes.TASK, now.plusDays(1), defaultDuration, now.plusDays(1).plusMinutes(defaultDuration));
        manager1.addTask(homeworkTask);
        manager1.addTask(carbonaraTask);
    }

    public void initEpics() {
        docEpic = new Epic("take care about health", "visit doctors", Status.NEW, 3, Collections.emptyList());
        workEpic = new Epic("find clients", "", Status.NEW, 4, Collections.emptyList());
        manager1.addEpic(docEpic);
        manager1.addEpic(workEpic);
    }

    public void initEpicsAndSubtasks() {
        docEpic = new Epic("take care about health", "visit doctors", Status.NEW, 3);
        workEpic = new Epic("find clients", "", Status.NEW, 4);
        manager1.addEpic(docEpic);
        manager1.addEpic(workEpic);
        docTask1 = new Subtask("go to surgeon", "get a surgeon consultation", Status.NEW, docEpic, 3, now.plusDays(2), defaultDuration);
        docTask2 = new Subtask("go to dermatologist", "get a dermatologist consultation", Status.NEW, docEpic, 4, now.plusDays(3), defaultDuration);
        workTask1 = new Subtask("find clients", "use profi.ru", Status.NEW, workEpic, 5, now.plusDays(4), defaultDuration);
        manager1.addSubtask(docTask1);
        manager1.addSubtask(docTask2);
        manager1.addSubtask(workTask1);
    }

    @Test
    public void shouldSaveTasksToFile() {
        initTasks();
        manager2 = createManager();
        assertEquals(manager1.getTasks(), manager2.getTasks(), "saved and loaded tasks dont match");
    }

    @Test
    public void shouldSaveEpicsWithSubtasks() {
        initEpicsAndSubtasks();
        Map<Integer, Epic> epics1 = manager1.getEpics();
        manager1.removeAll();
        manager1.loadData();
        Map<Integer, Epic> epics2 = manager1.getEpics();
        assertEquals(epics1, epics2, "saved and loaded epics do not match");
    }

    @Test
    public void shouldSaveEpicsWithSubtasks2() {
        initEpicsAndSubtasks();
        manager2 = new FileBackedTaskManager();
        manager2.setEpics(manager1.getEpics());
        manager2.setSubtasks(manager1.getSubtasks());
        assertEquals(manager1.getEpics(), manager2.getEpics());
        assertEquals(manager1.getSubtasks(), manager2.getSubtasks());
    }

    @Test
    public void shouldSaveHistory() {
        initTasks();
        initEpicsAndSubtasks();
        manager1.getTaskById(1);
        manager1.getTaskById(1);
        manager1.getSubtaskById(5);
        manager1.getEpicById(3);
        manager2 = createManager();
        assertEquals(manager1.getHistory(), manager2.getHistory(), "saved and loaded histories do not match");
    }
}
