package manager;

import manager.exceptions.InvalidIdException;
import manager.exceptions.NoSuchEpicException;
import manager.exceptions.NoTimeException;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    private ZonedDateTime now;
    private T manager;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private long defaultDuration;

    public abstract T createManager();

    @BeforeEach
    public void init() {
        manager = createManager();
        defaultDuration = 10;
        now = ZonedDateTime.now();
        task = new Task("task", "desc of task", Status.NEW, 1, now.plusDays(2), defaultDuration);
        epic = new Epic("epic", "desc of epic", Status.NEW, 2);
        subtask = new Subtask("subtask", "desc of subtask", Status.NEW, epic, 3, now.plusDays(1), defaultDuration);
        subtask.setEpicId(epic.getId());
    }

    @BeforeEach
    @AfterEach
    public void cleanUp() {
        manager.removeAll();
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
        addDefaultTasks();
        manager.generateAndSetTaskId(task);
        int oldId = task.getId();
        manager.generateAndSetTaskId(task);
        int newId = task.getId();
        assertEquals(newId - oldId, 1);
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
        assertThrows(InvalidIdException.class, () -> manager.removeEpicById(-epic.getId()));
        manager.removeEpicById(epic.getId());
        assertEquals(Collections.emptyMap(), manager.getEpics());
        assertEquals(Collections.emptyMap(), manager.getSubtasks());
        assertThrows(InvalidIdException.class, () -> manager.removeEpicById(epic.getId()));
    }

    @Test
    public void shouldRemoveAllTasks() {
        addDefaultTasks();
        assertNotEquals(Collections.emptyMap(), manager.getTasks());
        manager.removeAllTasks();
        assertEquals(Collections.emptyMap(), manager.getTasks(), "tasks were not removed");
    }

    @Test
    public void shouldRemoveAllEpics() {
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
        assertNotEquals(Collections.emptyMap(), manager.getSubtasks());
        manager.removeAllSubtasks();
        assertEquals(Collections.emptyMap(), manager.getSubtasks(), "subtasks were not removed");
    }

    @Test
    public void shouldUpdateTask() {
        manager.addTask(task);
        Task oldTask = manager.getTaskById(1);
        manager.updateTask(new Task("updated", "desc of task", Status.DONE, oldTask.getId(), now.plusDays(123), defaultDuration));
        Task updatedTask = manager.getTaskById(1);
        assertNotEquals(oldTask, updatedTask, "task was not updated");
    }

    @Test
    public void shouldUpdateEpic() {
        addDefaultTasks();
        Epic oldEpic = manager.getEpicById(2);
        manager.updateEpic(new Epic("updated", "desc of epic", Status.DONE, epic.getId()));
        Epic updatedEpic = manager.getEpicById(2);
        assertNotEquals(oldEpic, updatedEpic, "epic was not updated");
    }

    @Test
    public void shouldUpdateSubtask() {
        manager.addEpic(epic);
        subtask = new Subtask("subtask", "desc of subtask", Status.NEW, epic, 3, now.plusDays(1), defaultDuration);
        manager.addSubtask(subtask);
        Subtask oldSubtask = manager.getSubtaskById(subtask.getId());
        manager.updateSubtask(new Subtask("updated", "desc of subtask", Status.DONE, epic, subtask.getId(), now.plusDays(2), defaultDuration));
        Subtask updatedSubtask = manager.getSubtaskById(subtask.getId());
        assertNotEquals(oldSubtask, updatedSubtask, "subtask was not updated");
    }

    @Test
    public void shouldReturnPrioritizedTasks() {
        addDefaultTasks();
        Task t = new Task("s", "s", Status.NEW, 4, null, 0);
        manager.addTask(t);
        assertEquals(List.of(subtask, task, t), manager.getPrioritizedTasks());
    }

    @Test
    public void shouldThrowNoTimeExceptionWhenAddingTimeIntersectingTask() {
        Task anotherTask = new Task("task", "desc of task", Status.NEW, 1, now.plusDays(2), defaultDuration - 2);
        manager.addTask(anotherTask);
        assertThrows(NoTimeException.class, () -> {
            manager.addTask(task);
        });


    }

    @Test
    public void shouldThrowNoTimeExceptionWhenAddingDuplicateTask() {
        manager.addTask(task);
        assertThrows(NoTimeException.class, () -> manager.addTask(task));
    }

    @Test
    public void shouldReturnHistoryInCorrectOrder() {
        addDefaultTasks();
        manager.getTaskById(task.getId());
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());
        manager.getEpicById(epic.getId());
        manager.getSubtaskById(subtask.getId());
        assertEquals(List.of(task, epic, subtask), manager.getHistory(), "history is incorrect");
    }
}