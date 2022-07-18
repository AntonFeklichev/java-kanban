package manager.history;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryTest {
    private HistoryManager historyManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void init() {
        historyManager = Managers.getDefaultHistory();
        task = new Task(1);
        epic = new Epic(2);
        subtask = new Subtask(3);
    }

    public void addDefaultTasks() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
    }

    @Test
    public void shouldAddToHistory() {
        addDefaultTasks();
        assertEquals(List.of(task, epic, subtask), historyManager.getHistory());
    }

    @Test
    public void shouldRemoveFromStartOfHistory() {
        addDefaultTasks();
        historyManager.remove(1);
        assertEquals(List.of(epic, subtask), historyManager.getHistory());
    }

    @Test
    public void shouldRemoveFromMidOfHistory() {
        addDefaultTasks();
        historyManager.remove(2);
        assertEquals(List.of(task, subtask), historyManager.getHistory());
    }

    @Test
    public void shouldRemoveFromEndOfHistory() {
        addDefaultTasks();
        historyManager.remove(3);
        assertEquals(List.of(task, epic), historyManager.getHistory());
    }

    @Test
    public void shouldRemoveDoublingTasksFromHistory() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task);
        assertEquals(List.of(epic, task), historyManager.getHistory());
    }

    @Test
    public void shouldReturnEmptyListWhenEmptyHistory() {
        assertEquals(Collections.emptyList(), historyManager.getHistory());
    }
}
