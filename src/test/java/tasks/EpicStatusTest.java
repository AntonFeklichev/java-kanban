package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {
    private TaskManager manager;
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;
    private ZonedDateTime now;
    private long defaultDuration;

    @BeforeEach
    public void init() {
        manager = Managers.getDefault();
        now = ZonedDateTime.now();
        defaultDuration = 10;
        epic = new Epic();
        manager.addEpic(epic);
        subtask1 = new Subtask(Status.NEW, epic, now, defaultDuration);
        manager.addSubtask(subtask1);
        subtask2 = new Subtask(Status.NEW, epic, now.plusDays(1), defaultDuration);
        manager.addSubtask(subtask2);
        subtask3 = new Subtask(Status.NEW, epic, now.plusDays(2), defaultDuration);
        manager.addSubtask(subtask3);
    }

    @Test
    public void shouldReturnNewWhenNoSubtasks() {
        epic.setSubtasks(Collections.emptyList());
        assertEquals(Status.NEW, epic.calculateStatus());
    }

    @Test
    public void shouldReturnStatusNewWhenAllSubtasksNew() {
        assertEquals(Status.NEW, epic.calculateStatus());
    }

    @Test
    public void shouldReturnDoneWhenAllSubtasksDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        assertEquals(Status.DONE, epic.calculateStatus());
    }

    @Test
    public void shouldReturnInProgressWhenSubtasksNewAndDone() {
        subtask3.setStatus(Status.DONE);
        assertEquals(Status.IN_PROGRESS, epic.calculateStatus());
    }

    @Test
    public void shouldReturnInProgressWhenAtLeastOneInProgress() {
        subtask3.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, epic.calculateStatus());
    }
}
