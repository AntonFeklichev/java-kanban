package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int idForNewTasks = 1;
    private int idForNewSubtasks = 1;
    private int idForNewEpics = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private List<Task> history = new ArrayList<>();

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
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
        if (tasks.get(id) != null) {
            if (history.size() == 10){
                history.remove(0);
            }
            history.add(tasks.get(id));
        }
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.get(id) != null) {
            if (history.size() == 10){
                history.remove(0);
            }
            history.add(subtasks.get((id)));
        }
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) != null) {
            if (history.size() == 10){
                history.remove(0);
            }
            history.add(epics.get(id));
        }
        return epics.get(id);
    }

    @Override
    public void generateAndSetTaskId(Task task) {
        task.setId(idForNewTasks);
        idForNewTasks++;
    }

    public void generateAndSetSubtaskId(Subtask subtask) {
        subtask.setId(idForNewSubtasks);
        idForNewSubtasks++;
    }

    public void generateAndSetEpicId(Epic epic) {
        epic.setId(idForNewEpics);
        idForNewEpics++;
    }

    @Override
    public void addTask(Task task) {
        generateAndSetTaskId(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            generateAndSetSubtaskId(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().add(subtask);
            epic.setStatus(calculateStatus(epic));
            subtasks.put(subtask.getId(), subtask);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        generateAndSetEpicId(epic);
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
            epic.setStatus(calculateStatus(epic));
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
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int epicId) {
        for (Subtask subtask : epics.get(epicId).getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(epicId);
    }

    @Override
    public void removeSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask subtask = subtasks.get(subtaskId);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask);
            epic.setStatus(calculateStatus(epic));
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public Status calculateStatus(Epic epic) {
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;
        for (Subtask subtask : epic.getSubtasks()) {
            if (subtask.getStatus().equals(Status.NEW)) {
                newCounter++;
            } else if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                inProgressCounter++;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                doneCounter++;
            }
        }
        if (newCounter == epic.getSubtasks().size() || epic.getSubtasks().size() == 0) return Status.NEW;
        else if (doneCounter == epic.getSubtasks().size()) return Status.DONE;
        else if (inProgressCounter > 0 || doneCounter > 0) return Status.IN_PROGRESS;
        return epic.getStatus();
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
