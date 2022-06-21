package tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String desc, Status status) {
        super(name, desc, status, TaskTypes.EPIC);
    }

    public Epic(String name, String desc, Status status, int id) {
        super(name, desc, status, id);
    }

    public Epic(String name, String desc, Status status, int id, List<Subtask> subtasks) {
        super(name, desc, status, id);
        this.subtasks = subtasks;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + "name=" + '\'' + getName() + '\'' + ", desc=" + '\'' + super.getDesc() + '\'' + ", id=" + getId() + ", status=" + getStatus() + ", subtasks=" + subtasks + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() == obj.getClass()) {
            Epic otherEpic = (Epic) obj;
            return super.equals(obj) && subtasks.equals(otherEpic.getSubtasks());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDesc(), getSubtasks());
    }
}
