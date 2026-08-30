package Models;

import java.util.ArrayList;
import java.util.List;

public class Election {
    private String id;
    private String title;
    private ElectionStatus status;
    private List<Integer> rankingPoints;
    private int duplicatePatternThreshold;

    public Election() {
        this.rankingPoints = new ArrayList<>();
    }

    public Election(String id, String title, ElectionStatus status, List<Integer> rankingPoints,
            int duplicatePatternThreshold) {
        this.id = id;
        this.title = title;
        this.status = status;
        if (rankingPoints != null) {
            this.rankingPoints = rankingPoints;
        } else {
            this.rankingPoints = new ArrayList<>();
        }
        this.duplicatePatternThreshold = duplicatePatternThreshold;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ElectionStatus getStatus() {
        return status;
    }

    public void setStatus(ElectionStatus status) {
        this.status = status;
    }

    public List<Integer> getRankingPoints() {
        return rankingPoints;
    }

    public void setRankingPoints(List<Integer> rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    public int getDuplicatePatternThreshold() {
        return duplicatePatternThreshold;
    }

    public void setDuplicatePatternThreshold(int duplicatePatternThreshold) {
        this.duplicatePatternThreshold = duplicatePatternThreshold;
    }
}
