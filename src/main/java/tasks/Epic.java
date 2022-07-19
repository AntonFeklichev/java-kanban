package tasks;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class Epic extends Task {

    private List<Subtask> subtasks = new LinkedList<>();

    public Epic(String name, String desc, Status status) {
        super(name, desc, status, TaskTypes.EPIC);
    }

    public Epic(String name, String desc, Status status, int id) {
        super(name, desc, status, id);
        setType(TaskTypes.EPIC);
    }

    public Epic(String name, String desc, Status status, int id, List<Subtask> subtasks) {
        super(name, desc, status, id);
        this.subtasks = subtasks;
        setType(TaskTypes.EPIC);
    }

    public Epic(int id) {
        setId(id);
    }

    public ZonedDateTime calculateStartTime() {
        try {
            Optional<ZonedDateTime> calculated = subtasks.stream().map(Subtask::getStartTime).min(ChronoZonedDateTime::compareTo);
            return calculated.orElse(getEndTime());
        } catch (NullPointerException ex) {
            return getEndTime();
        }
    }
    public long calculateDuration() {
        return subtasks.stream().mapToLong(Subtask::getDuration).sum();
    }

    @Override
    public ZonedDateTime calculateEndTime() {
        try {
            Optional<ZonedDateTime> calculated = subtasks.stream().map(Subtask::getEndTime).max(ChronoZonedDateTime::compareTo);
            return calculated.orElse(getEndTime());
        } catch (NullPointerException ex) {
            return getEndTime();
        }
    }

    public Status calculateStatus() {
        int newCounter = 0;
        int inProgressCounter = 0;
        int doneCounter = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus().equals(Status.NEW)) {
                newCounter++;
            } else if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                inProgressCounter++;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                doneCounter++;
            }
        }
        if (newCounter == subtasks.size() || subtasks.size() == 0) return Status.NEW;
        else if (doneCounter == subtasks.size()) return Status.DONE;
        else if (inProgressCounter > 0 || doneCounter > 0) return Status.IN_PROGRESS;
        return getStatus();
    }


    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", desc='" + getDesc() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", type=" + getType() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                ", subtasks=" + getSubtasks() +
                '}';
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
