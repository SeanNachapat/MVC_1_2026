package Models;

import java.util.ArrayList;
import java.util.List;

public class BallotGroup {
    private String groupId;
    private List<String> ranking;
    private List<String> ballotIds;
    private BallotStatus status;

    public BallotGroup() {
        this.ranking = new ArrayList<>();
        this.ballotIds = new ArrayList<>();
        this.status = BallotStatus.PENDING_INSPECTION;
    }

    public BallotGroup(String groupId, List<String> ranking, List<String> ballotIds) {
        this.groupId = groupId;
        if (ranking == null || ranking.isEmpty()) {
            throw new IllegalArgumentException("Ranking can't be empty");
        }
        if (ballotIds == null || ballotIds.isEmpty()) {
            throw new IllegalArgumentException("Ballot IDs can't be empty");
        }
        this.ranking = ranking;
        this.ballotIds = ballotIds;
        this.status = BallotStatus.PENDING_INSPECTION;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getRanking() {
        return ranking;
    }

    public void setRanking(List<String> ranking) {
        this.ranking = ranking;
    }

    public List<String> getBallotIds() {
        return ballotIds;
    }

    public void setBallotIds(List<String> ballotIds) {
        this.ballotIds = ballotIds;
    }

    public BallotStatus getStatus() {
        return status;
    }

    public void setStatus(BallotStatus status) {
        this.status = status;
    }

    public int getBallotCount() {
        return ballotIds != null ? ballotIds.size() : 0;
    }

    public boolean isPending() {
        return status == BallotStatus.PENDING_INSPECTION;
    }

    public String getRankingPatternKey() {
        return ranking != null ? String.join(" -> ", ranking) : "";
    }
}

