package manager.history;

import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

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
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
    }

    @Test
    public void shouldAddToHistory(){
        assertEquals(List.of(task, epic, subtask), historyManager.getHistory());
    }

    @Test
    public void shouldRemoveFromHistory(){
        historyManager.remove(2);
        assertEquals(List.of(task, subtask), historyManager.getHistory());
    }
}
