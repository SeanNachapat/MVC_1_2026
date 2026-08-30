package Models;

import java.util.ArrayList;
import java.util.List;

public class ElectionData {
    private Election election;
    private List<Officers> officers;
    private List<Candidate> candidates;
    private List<Voter> voters;
    private List<Ballot> ballots;

    public ElectionData() {
        this.officers = new ArrayList<>();
        this.candidates = new ArrayList<>();
        this.voters = new ArrayList<>();
        this.ballots = new ArrayList<>();
    }

    public ElectionData(Election election, List<Officers> officers, List<Candidate> candidates, List<Voter> voters, List<Ballot> ballots) {
        this.election = election;
        if (officers != null) {
            this.officers = officers;
        } else {
            this.officers = new ArrayList<>();
        }
        if (candidates != null) {
            this.candidates = candidates;
        } else {
            this.candidates = new ArrayList<>();
        }
        if (voters != null) {
            this.voters = voters;
        } else {
            this.voters = new ArrayList<>();
        }
        if (ballots != null) {
            this.ballots = ballots;
        } else {
            this.ballots = new ArrayList<>();
        }
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public List<Officers> getOfficers() {
        return officers;
    }

    public void setOfficers(List<Officers> officers) {
        this.officers = officers;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public void setVoters(List<Voter> voters) {
        this.voters = voters;
    }

    public List<Ballot> getBallots() {
        return ballots;
    }

    public void setBallots(List<Ballot> ballots) {
        this.ballots = ballots;
    }

    @Override
    public String toString() {
        return String.format("ElectionData[election=%s, officers=%d, candidates=%d, voters=%d, ballots=%d]",
                election != null ? election.getTitle() : "null",
                officers != null ? officers.size() : 0,
                candidates != null ? candidates.size() : 0,
                voters != null ? voters.size() : 0,
                ballots != null ? ballots.size() : 0);
    }
}
