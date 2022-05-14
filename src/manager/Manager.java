package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int idForNewTasks = 1;
    private int idForNewSubtasks = 1;
    private int idForNewEpics = 1;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

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

    public void addTask(Task task) {
        generateAndSetTaskId(task);
        tasks.put(task.getId(), task);
    }

    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            generateAndSetSubtaskId(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().add(subtask);
            epic.setStatus(calculateStatus(epic));
            subtasks.put(subtask.getId(), subtask);
        }
    }

    public void addEpic(Epic epic) {
        generateAndSetEpicId(epic);
        epics.put(epic.getId(), epic);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.replace(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask);
            epic.getSubtasks().add(subtask);
            subtasks.replace(subtask.getId(), subtask);
            epic.setStatus(calculateStatus(epic));
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.setSubtasks(epics.get(epic.getId()).getSubtasks());
            epics.replace(epic.getId(), epic);
        }
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int epicId) {
        for (Subtask subtask: epics.get(epicId).getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(epicId);
    }

    public void removeSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask subtask = subtasks.get(subtaskId);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask);
            epic.setStatus(calculateStatus(epic));
            subtasks.remove(subtaskId);
        }
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return epic.getSubtasks();
    }

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
}
