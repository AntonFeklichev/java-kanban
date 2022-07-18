package manager;

import manager.exceptions.InvalidIdException;
import manager.exceptions.NoSuchEpicException;
import manager.history.HistoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int idForNewTasks = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public void setEpics(Map<Integer, Epic> epics) {
        this.epics = epics;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Map<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void generateAndSetTaskId(Task task) {
        task.setId(idForNewTasks);
        idForNewTasks++;
    }

    @Override
    public void addTask(Task task) {
        generateAndSetTaskId(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            generateAndSetTaskId(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().add(subtask);
            epic.setStatus(epic.calculateStatus());
            subtasks.put(subtask.getId(), subtask);
        } else {
            throw new NoSuchEpicException("не добавлен эпик к которому относится добавляемая подазадача");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        generateAndSetTaskId(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.replace(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask);
            epic.getSubtasks().add(subtask);
            subtasks.replace(subtask.getId(), subtask);
            epic.setStartTime(epic.calculateStartTime());
            epic.setDuration(epic.calculateDuration());
            epic.setEndTime(epic.calculateEndTime());
            epic.setStatus(epic.calculateStatus());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setSubtasks(epics.get(epic.getId()).getSubtasks());
            epics.replace(epic.getId(), epic);
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.remove(id) == null)
            throw new InvalidIdException(String.format("task with id = %d does not exist", id));
    }

    @Override
    public void removeEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            epics.remove(epicId);
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        } else throw new InvalidIdException(String.format("epic with id = %d does not exist", epicId));
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask subtask = subtasks.get(subtaskId);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask);
            epic.setStartTime(epic.calculateStartTime());
            epic.setDuration(epic.calculateDuration());
            epic.setEndTime(epic.calculateEndTime());
            epic.setStatus(epic.calculateStatus());
            subtasks.remove(subtaskId);
        } else {
            throw new InvalidIdException(String.format("subtask with id = %d does not exist", subtaskId));
        }
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override


    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
