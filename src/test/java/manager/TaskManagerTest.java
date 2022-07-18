package manager;

import manager.exceptions.InvalidIdException;
import manager.exceptions.NoSuchEpicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

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

    public void addDefaultTasks() {
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
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

    @Test
    public void shouldGenerateAndSetTaskId() {
        manager.generateAndSetTaskId(task);
        int oldId = task.getId();
        manager.generateAndSetTaskId(task);
        int newId = task.getId();
        assertEquals(1, newId - oldId);
    }

    @Test
    public void shouldRemoveTask() {
        manager.addTask(task);
        assertThrows(InvalidIdException.class, () -> manager.removeTaskById(-task.getId()));
        manager.removeTaskById(task.getId());
        assertEquals(Collections.emptyMap(), manager.getTasks());
        assertThrows(InvalidIdException.class, () -> manager.removeTaskById(task.getId()));
    }

    @Test
    public void shouldRemoveSubtask() {
        manager.addEpic(epic);
        subtask.setEpicId(epic.getId());
        manager.addSubtask(subtask);
        assertThrows(InvalidIdException.class, () -> manager.removeSubtaskById(-subtask.getId()));
        manager.removeSubtaskById(subtask.getId());
        assertEquals(Collections.emptyMap(), manager.getSubtasks());
        assertThrows(InvalidIdException.class, () -> manager.removeSubtaskById(subtask.getId()));
    }

    @Test
    public void shouldRemoveEpicWithItsSubtasks() {
        manager.addEpic(epic);
        subtask.setEpicId(epic.getId());
        manager.addSubtask(subtask);
        System.out.println(manager.getEpics());
        assertThrows(InvalidIdException.class, () -> manager.removeEpicById(-epic.getId()));
        manager.removeEpicById(epic.getId());
        assertEquals(Collections.emptyMap(), manager.getEpics());
        assertEquals(Collections.emptyMap(), manager.getSubtasks());
        assertThrows(InvalidIdException.class, () -> manager.removeEpicById(epic.getId()));
    }

    @Test
    public void shouldRemoveAllTasks() {
        addDefaultTasks();
        addDefaultTasks();
        assertNotEquals(Collections.emptyMap(), manager.getTasks());
        manager.removeAllTasks();
        assertEquals(Collections.emptyMap(), manager.getTasks());
    }

    @Test
    public void shouldRemoveAllEpics() {
        addDefaultTasks();
        addDefaultTasks();
        assertNotEquals(Collections.emptyMap(), manager.getEpics());
        assertNotEquals(Collections.emptyMap(), manager.getSubtasks());
        manager.removeAllEpics();
        assertEquals(Collections.emptyMap(), manager.getEpics());
        manager.removeAllSubtasks();
        assertEquals(Collections.emptyMap(), manager.getSubtasks());
    }

    @Test
    public void shouldRemoveAllSubtasks() {
        addDefaultTasks();
        addDefaultTasks();
        assertNotEquals(Collections.emptyMap(), manager.getSubtasks());
        manager.removeAllSubtasks();
        assertEquals(Collections.emptyMap(), manager.getSubtasks());
    }

}