package Models;

public class Voter {
    private String id;
    private String name;
    private boolean active;
    private boolean hasVoted;

    public Voter() {
    }

    public Voter(String id, String name, boolean active) {
        this(id, name, active, false);
    }

    public Voter(String id, String name, boolean active, boolean hasVoted) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.hasVoted = hasVoted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}

