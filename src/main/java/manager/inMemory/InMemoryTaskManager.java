package manager.inMemory;

import manager.Managers;
import manager.TaskManager;
import manager.exceptions.InvalidIdException;
import manager.exceptions.NoSuchEpicException;
import manager.exceptions.NoTimeException;
import manager.history.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.ZonedDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Set<Task> sorted = new TreeSet<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int idForNewTasks = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    public InMemoryTaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

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
        sorted.removeAll(tasks.values());
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        sorted.removeAll(subtasks.values());
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
        task.setId(idForNewTasks++);
    }

    @Override
    public void addTask(Task task) {
        checkTimeIntersection(task);
        generateAndSetTaskId(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            checkTimeIntersection(subtask);
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
            sorted.remove(tasks.get(task.getId()));
            checkTimeIntersection(task);
            sorted.add(task);
            tasks.replace(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask);
            epic.getSubtasks().add(subtask);
            sorted.remove(subtasks.get(subtask.getId()));
            checkTimeIntersection(subtask);
            sorted.add(subtask);
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
        if (tasks.containsKey(id)) {
            sorted.remove(tasks.get(id));
            tasks.remove(id);
        } else {
            throw new InvalidIdException(String.format("task with id = %d does not exist", id));
        }
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
            sorted.remove(subtasks.get(subtaskId));
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

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> timeIsNull = new ArrayList<>();
        tasks.values().forEach(task -> {
            if (!(task.getStartTime() == null || task.getEndTime() == null || task.getStartTime().isAfter(task.getEndTime()))) {
                sorted.add(task);
            } else timeIsNull.add(task);
        });
        subtasks.values().forEach(subtask -> {
            if (!(subtask.getStartTime() == null || subtask.getEndTime() == null || subtask.getStartTime().isAfter(subtask.getEndTime()))) {
                sorted.add(subtask);
            } else timeIsNull.add(subtask);
        });
        List<Task> prioritized = new ArrayList<>();
        prioritized.addAll(sorted);
        prioritized.addAll(timeIsNull);
        return prioritized;
    }

    @Override
    public Map<Integer, Task> getTasksOfAllTypes() {
        Map<Integer, Task> tasksOfAllTypes = new HashMap<>();
        tasksOfAllTypes.putAll(tasks);
        tasksOfAllTypes.putAll(epics);
        tasksOfAllTypes.putAll(subtasks);
        return tasksOfAllTypes;
    }

    public void checkTimeIntersection(Task task) {
        ZonedDateTime startTime = task.getStartTime();
        ZonedDateTime endTime = task.getEndTime();
        if (startTime == null || endTime == null || startTime.isAfter(endTime)) {
            return;
        } else if (getPrioritizedTasks().stream().anyMatch(t -> (t.getStartTime().isAfter(startTime) || t.getStartTime().isEqual(startTime)) && (t.getEndTime().isBefore(endTime) || t.getEndTime().isEqual(endTime)))) {
            throw new NoTimeException("time is already occupied with another task");
        }
    }
    @Override
    public void removeAll(){
        removeAllTasks();
        removeAllEpics();
        removeAllSubtasks();
        getHistoryManager().removeAll();
    }
}
