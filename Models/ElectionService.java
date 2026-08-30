package Models;

import java.util.*;

public class ElectionService {
    private Election election;
    private final Map<String, Candidate> candidates = new LinkedHashMap<>();
    private final Map<String, Voter> voters = new LinkedHashMap<>();
    private final Map<String, Officers> officers = new LinkedHashMap<>();
    private final List<Ballot> ballots = new ArrayList<>();
    private final Map<String, BallotGroup> ballotGroups = new LinkedHashMap<>();
    private int ballotCounter = 1;

    public ElectionService() {
        this.election = new Election("E01", "General Election", ElectionStatus.OPEN, Arrays.asList(3, 2, 1), 3);
    }

    public ElectionService(ElectionData data) {
        this();
        if (data != null) {
            loadFromData(data);
        }
    }

    public void loadFromData(ElectionData data) {
        if (data.getElection() != null) {
            this.election = data.getElection();
            if (this.election.getStatus() == null) {
                this.election.setStatus(ElectionStatus.OPEN);
            }
            if (this.election.getRankingPoints() == null || this.election.getRankingPoints().isEmpty()) {
                this.election.setRankingPoints(Arrays.asList(3, 2, 1));
            }
            if (this.election.getDuplicatePatternThreshold() <= 0) {
                this.election.setDuplicatePatternThreshold(3);
            }
        }

        candidates.clear();
        if (data.getCandidates() != null) {
            for (Candidate c : data.getCandidates()) {
                candidates.put(c.getId(), c);
            }
        }

        voters.clear();
        if (data.getVoters() != null) {
            for (Voter v : data.getVoters()) {
                voters.put(v.getId(), v);
            }
        }

        officers.clear();
        if (data.getOfficers() != null) {
            for (Officers o : data.getOfficers()) {
                officers.put(o.getId(), o);
            }
        }

        ballots.clear();
        ballotGroups.clear();
        if (data.getBallots() != null) {
            for (Ballot b : data.getBallots()) {
                if (b.getStatus() == null) {
                    b.setStatus(BallotStatus.CERTIFIED);
                }
                ballots.add(b);
                Voter v = voters.get(b.getVoterId());
                if (v != null) {
                    v.setHasVoted(true);
                }
                try {
                    String idNum = b.getId().replaceAll("\\D+", "");
                    int num = Integer.parseInt(idNum);
                    if (num >= ballotCounter) {
                        ballotCounter = num + 1;
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    public Election getElection() {
        return election;
    }

    public ElectionStatus getStatus() {
        return election.getStatus();
    }

    public List<Candidate> getAllCandidates() {
        return new ArrayList<>(candidates.values());
    }

    public List<Ballot> getAllBallots() {
        return new ArrayList<>(ballots);
    }

    public List<BallotGroup> getAllBallotGroups() {
        return new ArrayList<>(ballotGroups.values());
    }

    public List<BallotGroup> getPendingBallotGroups() {
        List<BallotGroup> list = new ArrayList<>();
        for (BallotGroup pg : ballotGroups.values()) {
            if (pg.isPending()) {
                list.add(pg);
            }
        }
        return list;
    }

    public BallotGroup getBallotGroup(String groupId) {
        return ballotGroups.get(groupId);
    }

    public Ballot castVote(String voterId, List<String> rankedIds) throws IllegalArgumentException, IllegalStateException {
        if (this.election.getStatus() != ElectionStatus.OPEN) {
            throw new IllegalStateException("Election is not in OPEN status.");
        }

        Voter voter = voters.get(voterId);
        if (voter == null) {
            throw new IllegalArgumentException("Voter not found: " + voterId);
        }
        if (!voter.isActive()) {
            throw new IllegalArgumentException("Voter " + voterId + " is not Active.");
        }
        if (voter.hasVoted()) {
            throw new IllegalArgumentException("Voter " + voterId + " has already voted.");
        }

        if (rankedIds == null || rankedIds.size() != 3) {
            throw new IllegalArgumentException("Must rank exactly 3 candidates.");
        }

        Set<String> uniqueSelections = new LinkedHashSet<>(rankedIds);
        if (uniqueSelections.size() != 3) {
            throw new IllegalArgumentException("Candidate selections must be distinct.");
        }

        for (String cId : rankedIds) {
            if (!candidates.containsKey(cId)) {
                throw new IllegalArgumentException("Candidate not found: " + cId);
            }
        }

        String ballotId = String.format("B%02d", ballotCounter++);
        Ballot newBallot = new Ballot(ballotId, voterId, new ArrayList<>(rankedIds), BallotStatus.CERTIFIED, null);
        ballots.add(newBallot);
        voter.setHasVoted(true);
        return newBallot;
    }

    public void closePollsAndDetectPatterns() throws IllegalStateException {
        if (this.election.getStatus() != ElectionStatus.OPEN) {
            throw new IllegalStateException("Election is not in OPEN status.");
        }

        this.election.setStatus(ElectionStatus.CLOSED);

        ballotGroups.clear();
        Map<String, List<Ballot>> patternMap = new LinkedHashMap<>();
        for (Ballot b : ballots) {
            String key = b.getPatternKey();
            if (!patternMap.containsKey(key)) {
                patternMap.put(key, new ArrayList<>());
            }
            patternMap.get(key).add(b);
        }

        int threshold = this.election.getDuplicatePatternThreshold();
        if (threshold <= 0) threshold = 3;

        int groupCounter = 1;
        for (Map.Entry<String, List<Ballot>> entry : patternMap.entrySet()) {
            List<Ballot> matchingBallots = entry.getValue();
            if (matchingBallots.size() >= threshold) {
                String gId = String.format("PG%02d", groupCounter++);
                List<String> bIds = new ArrayList<>();
                for (Ballot b : matchingBallots) {
                    bIds.add(b.getId());
                    b.setStatus(BallotStatus.PENDING_INSPECTION);
                    b.setPatternGroupId(gId);
                }
                BallotGroup group = new BallotGroup(gId, matchingBallots.get(0).getRanking(), bIds);
                ballotGroups.put(gId, group);
            } else {
                for (Ballot b : matchingBallots) {
                    b.setStatus(BallotStatus.CERTIFIED);
                    b.setPatternGroupId(null);
                }
            }
        }

        if (ballotGroups.isEmpty()) {
            this.election.setStatus(ElectionStatus.CONCLUDED);
        }
    }

    public void decidePatternGroup(String groupId, boolean certify) throws IllegalArgumentException, IllegalStateException {
        if (this.election.getStatus() == ElectionStatus.OPEN) {
            throw new IllegalStateException("Election is still OPEN.");
        }
        if (this.election.getStatus() == ElectionStatus.CONCLUDED) {
            throw new IllegalStateException("Election is already CONCLUDED.");
        }

        BallotGroup group = ballotGroups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Duplicate ballot group not found: " + groupId);
        }

        if (!group.isPending()) {
            throw new IllegalStateException("Group " + groupId + " is already decided (" + group.getStatus() + ").");
        }

        BallotStatus newStatus = certify ? BallotStatus.CERTIFIED : BallotStatus.VOID;
        group.setStatus(newStatus);

        for (String bId : group.getBallotIds()) {
            for (Ballot b : ballots) {
                if (b.getId().equals(bId)) {
                    b.setStatus(newStatus);
                    break;
                }
            }
        }

        boolean allDecided = true;
        for (BallotGroup pg : ballotGroups.values()) {
            if (pg.isPending()) {
                allDecided = false;
                break;
            }
        }

        if (allDecided) {
            this.election.setStatus(ElectionStatus.CONCLUDED);
        }
    }

    public Map<String, Integer> calculateScores() {
        Map<String, Integer> scores = new LinkedHashMap<>();
        for (String cId : candidates.keySet()) {
            scores.put(cId, 0);
        }

        List<Integer> points = election.getRankingPoints();
        int p1 = (points != null && points.size() > 0) ? points.get(0) : 3;
        int p2 = (points != null && points.size() > 1) ? points.get(1) : 2;
        int p3 = (points != null && points.size() > 2) ? points.get(2) : 1;
        int[] weights = new int[]{p1, p2, p3};

        for (Ballot b : ballots) {
            if (b.getStatus() == BallotStatus.CERTIFIED) {
                List<String> ranking = b.getRanking();
                for (int i = 0; i < ranking.size() && i < weights.length; i++) {
                    String cId = ranking.get(i);
                    scores.put(cId, scores.getOrDefault(cId, 0) + weights[i]);
                }
            }
        }
        return scores;
    }

    public int getTotalBallotCount() {
        return ballots.size();
    }

    public int getCertifiedBallotCount() {
        int count = 0;
        for (Ballot b : ballots) {
            if (b.getStatus() == BallotStatus.CERTIFIED) count++;
        }
        return count;
    }

    public int getPendingBallotCount() {
        int count = 0;
        for (Ballot b : ballots) {
            if (b.getStatus() == BallotStatus.PENDING_INSPECTION) count++;
        }
        return count;
    }

    public int getVoidBallotCount() {
        int count = 0;
        for (Ballot b : ballots) {
            if (b.getStatus() == BallotStatus.VOID) count++;
        }
        return count;
    }

    public ElectionData toElectionData() {
        return new ElectionData(
            this.election,
            new ArrayList<>(this.officers.values()),
            new ArrayList<>(this.candidates.values()),
            new ArrayList<>(this.voters.values()),
            new ArrayList<>(this.ballots)
        );
    }
}