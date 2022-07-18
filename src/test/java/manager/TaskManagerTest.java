package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {
    private T manager;
    public abstract T createManager();
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void init() {
        manager = createManager();
        task = new Task(1);
        epic = new Epic(2);
        subtask = new Subtask(3, epic);
    }

    @Test
    public void shouldAddNewTask() {
        manager.addTask(task);
        assertEquals(new HashMap<Integer, Task>() {{
            put(task.getId(), task);
        }}, manager.getTasks());
    }
}
