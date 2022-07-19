import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeTest {
    ZonedDateTime startTime = ZonedDateTime.now();
    private Task task;
    private Epic epic;
    private TaskManager manager;

    @BeforeEach
    public void init() {
        epic = new Epic();
        manager = Managers.getDefault();
    }

    @Test
    public void taskTimeTest() {
        task = new Task(startTime, 121);
        assertEquals(startTime.plusMinutes(121), task.getEndTime());
    }

    @Test
    public void epicTimeTest() {
        Subtask subtask1 = new Subtask(startTime, 10);
        Subtask subtask2 = new Subtask(startTime.plusHours(2), 35);
        Subtask subtask3 = new Subtask(startTime.plusDays(4), 91);
        epic.setSubtasks(List.of(subtask1, subtask2, subtask3));
        epic.setStartTime(epic.calculateStartTime());
        epic.setDuration(epic.calculateDuration());
        epic.setEndTime(epic.calculateEndTime());
        assertEquals((subtask1.getDuration() + subtask2.getDuration() + subtask3.getDuration()), epic.getDuration());
        assertEquals(startTime, epic.getStartTime());
        assertEquals(startTime.plusDays(4).plusMinutes(91), epic.getEndTime());
    }
}
