package manager;

import manager.exceptions.NoSuchEpicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TaskManagerTest<T extends TaskManager> {
    private T manager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    public abstract T createManager();

    @BeforeEach
    public void init() {
        manager = createManager();
        task = new Task("task", "desc of task", Status.NEW, 1);
        epic = new Epic("epic", "desc of epic", Status.NEW, 2);
        subtask = new Subtask("subtask", "desc of subtask", Status.NEW, epic, 3);
        subtask.setEpicId(epic.getId());
    }

    @Test
    public void shouldAddNewTask() {
        manager.addTask(task);
        assertEquals(new HashMap<Integer, Task>() {{
            put(task.getId(), task);
        }}, manager.getTasks());
    }

    @Test
    public void shouldAddNewEpic() {
        manager.addEpic(epic);
        assertEquals(new HashMap<Integer, Epic>() {{
            put(epic.getId(), epic);
        }}, manager.getEpics());
    }

    @Test
    public void shouldAddNewSubtask() {
        manager.addEpic(epic);
        assertThrows(NoSuchEpicException.class, () -> manager.addSubtask(subtask));
        subtask.setEpicId(epic.getId());
        manager.addSubtask(subtask);
        assertEquals(new HashMap<Integer, Subtask>() {{
            put(subtask.getId(), subtask);
        }}, manager.getSubtasks());
    }

}
