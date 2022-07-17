package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {
    TaskManager manager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void init() {
        manager = Managers.getDefault();
        epic = new Epic();
        manager.addEpic(epic);
        subtask1 = new Subtask(Status.NEW, epic);
        manager.addSubtask(subtask1);
        subtask2 = new Subtask(Status.NEW, epic);
        manager.addSubtask(subtask2);
        subtask3 = new Subtask(Status.NEW, epic);
        manager.addSubtask(subtask3);
    }

    @Test
    public void shouldReturnNewWhenNoSubtasks(){
        epic.setSubtasks(Collections.emptyList());
        assertEquals(Status.NEW, manager.calculateStatus(epic));
    }

    @Test
    public void shouldReturnStatusNewWhenAllSubtasksNew(){
        assertEquals(Status.NEW, manager.calculateStatus(epic));
    }

    @Test
    public void shouldReturnDoneWhenAllSubtasksDone(){
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);
        assertEquals(Status.DONE, manager.calculateStatus(epic));
    }

    @Test
    public void shouldReturnInProgressWhenSubtasksNewAndDone(){
        subtask3.setStatus(Status.DONE);
        assertEquals(Status.IN_PROGRESS, manager.calculateStatus(epic));
    }

    @Test
    public void shouldReturnInProgressWhenAtLeastOneInProgress(){
        subtask3.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.calculateStatus(epic));
    }
}
