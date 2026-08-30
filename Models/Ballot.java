package Models;

import java.util.ArrayList;
import java.util.List;

public class Ballot {
    private String id;
    private String voterId;
    private List<String> ranking;
    private BallotStatus status;
    private String patternGroupId;

    public Ballot() {
        this.ranking = new ArrayList<>();
        this.status = BallotStatus.CERTIFIED;
    }

    public Ballot(String id, String voterId, List<String> ranking) {
        this(id, voterId, ranking, BallotStatus.CERTIFIED, null);
    }

    public Ballot(String id, String voterId, List<String> ranking, BallotStatus status, String patternGroupId) {
        this.id = id;
        this.voterId = voterId;
        if (ranking != null) {
            this.ranking = ranking;
        } else {
            this.ranking = new ArrayList<>();
        }
        if (status != null) {
            this.status = status;
        } else {
            this.status = BallotStatus.CERTIFIED;
        }
        this.patternGroupId = patternGroupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public List<String> getRanking() {
        return ranking;
    }

    public void setRanking(List<String> ranking) {
        this.ranking = ranking;
    }

    public BallotStatus getStatus() {
        return status;
    }

    public void setStatus(BallotStatus status) {
        this.status = status;
    }

    public String getPatternGroupId() {
        return patternGroupId;
    }

    public void setPatternGroupId(String patternGroupId) {
        this.patternGroupId = patternGroupId;
    }

    public String getPatternKey() {
        return ranking != null ? String.join(" -> ", ranking) : "";
    }
}
