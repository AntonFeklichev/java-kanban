package tasks;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Subtask> subtasks = new ArrayList<>();

    public Epic() {
    }

    ;

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

    public Epic(int id) {
        setId(id);
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

    @Override
    public long getDuration() {
        return subtasks.stream().mapToLong(Subtask::getDuration).sum();
    }

    @Override
    public ZonedDateTime getStartTime() {
        subtasks.stream().map(Subtask::getStartTime).min(ChronoZonedDateTime::compareTo).ifPresent(this::setStartTime);
        return super.getStartTime();
    }

    @Override
    public ZonedDateTime getEndTime() {
        subtasks.stream().map(Subtask::getEndTime).max(ChronoZonedDateTime::compareTo).ifPresent(this::setEndTime);
        return super.getEndTime();
    }
}
