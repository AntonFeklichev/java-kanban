package manager;

import manager.exceptions.InvalidIdException;
import manager.exceptions.NoSuchEpicException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        task = new Task("task", "desc of task", Status.NEW, 1, ZonedDateTime.now().plusDays(3), 10);
        epic = new Epic("epic", "desc of epic", Status.NEW, 2);
        subtask = new Subtask("subtask", "desc of subtask", Status.NEW, epic, 3, ZonedDateTime.now().plusHours(2), 20);
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
        assertNotEquals(newId, oldId, "id does not change after adding a new task");
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
        assertEquals(Collections.emptyMap(), manager.getTasks(), "tasks were not removed");
    }

    @Test
    public void shouldRemoveAllEpics() {
        addDefaultTasks();
        addDefaultTasks();
        assertNotEquals(Collections.emptyMap(), manager.getEpics());
        assertNotEquals(Collections.emptyMap(), manager.getSubtasks());
        manager.removeAllEpics();
        assertEquals(Collections.emptyMap(), manager.getEpics(), "epics were not removed");
        manager.removeAllSubtasks();
        assertEquals(Collections.emptyMap(), manager.getSubtasks(), "subtasks of the removed epics were not removed");
    }

    @Test
    public void shouldRemoveAllSubtasks() {
        addDefaultTasks();
        addDefaultTasks();
        assertNotEquals(Collections.emptyMap(), manager.getSubtasks());
        manager.removeAllSubtasks();
        assertEquals(Collections.emptyMap(), manager.getSubtasks(), "subtasks were not removed");
    }

    @Test
    public void shouldUpdateTask(){
        addDefaultTasks();
        Task oldTask = manager.getTaskById(1);
        manager.updateTask(new Task("updated", "desc of task", Status.DONE, 1));
        Task updatedTask = manager.getTaskById(1);
        assertNotEquals(oldTask, updatedTask, "task was not updated");
    }

    @Test
    public void shouldUpdateEpic(){
        addDefaultTasks();
        Epic oldEpic = manager.getEpicById(2);
        manager.updateEpic(new Epic("updated", "desc of epic", Status.DONE, epic.getId()));
        Epic updatedEpic = manager.getEpicById(2);
        assertNotEquals(oldEpic, updatedEpic, "epic was not updated");
    }

    @Test
    public void shouldUpdateSubtask(){
        addDefaultTasks();
        Subtask oldSubtask = manager.getSubtaskById(3);
        manager.updateSubtask(new Subtask("updated", "desc of subtask", Status.DONE, epic, subtask.getId()));
        Subtask updatedSubtask = manager.getSubtaskById(3);
        assertNotEquals(oldSubtask, updatedSubtask, "subtask was not updated");
    }

    @Test
    public void shouldReturnPrioritizedTasks(){
        addDefaultTasks();
        assertEquals(Set.of(subtask, task), manager.getPrioritizedTasks());
    }

}