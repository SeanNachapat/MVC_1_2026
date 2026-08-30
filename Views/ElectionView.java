package Views;

import Models.*;
import java.util.*;

public class ElectionView {

    public void title() {
        System.out.println("\n==================================");
        System.out.println("Very Really Transparent Club's President Election");
        System.out.println("==================================");
    }

    public void mainMenu() {
        System.out.println("\n==================================");
        System.out.println("Main Menu");
        System.out.println("==================================");
        System.out.println(" 1. View Candidates");
        System.out.println(" 2. Cast Vote");
        System.out.println(" 3. Close Polls & Detect Duplicate Patterns");
        System.out.println(" 4. Inspect & Decide Duplicate Ballot Groups");
        System.out.println(" 5. Election Status Summary & Results");
        System.out.println(" 6. View All Ballots (Traceability)");
        System.out.println(" 0. Exit");
        System.out.print("Select menu (0-6): ");
    }

    public void exit() {
        System.out.println("\nBye!");
    }

    public void votingTitle() {
        System.out.println("\n==================================");
        System.out.println("Cast Vote");
        System.out.println("==================================");
    }

    public void votingAudit() {
        System.out.println("\n==================================");
        System.out.println("Inspect & Decide Duplicate Ballot Groups");
        System.out.println("==================================");
    }

    public void votingAuditMenu(String groupId, int ballotCount) {
        System.out.println("Select decision for group " + groupId + " (" + ballotCount + " ballots):");
        System.out.println(" 1. CERTIFIED");
        System.out.println(" 2. VOID");
        System.out.print("Select (1 or 2): ");
    }

    public void candidates(List<Candidate> candidates) {
        System.out.println("\n==================================");
        System.out.println("Candidate List");
        System.out.println("==================================");
        System.out.printf("ID        Name%n");
        System.out.println("==================================");
        if (candidates == null || candidates.isEmpty()) {
            System.out.println("No candidate data.");
        } else {
            for (Candidate c : candidates) {
                System.out.printf("%-8s  %s%n", c.getId(), c.getName());
            }
        }
        System.out.println("==================================");
    }

    public void summary(ElectionService model) {
        Election election = model.getElection();
        ElectionStatus status = model.getStatus();

        System.out.println("\n==================================");
        System.out.println("Election Status Summary");
        System.out.println("==================================");
        System.out.printf("Election : %s (ID: %s)%n", election.getTitle(), election.getId());
        if (status != null) {
            System.out.printf("Status   : %s%n", status);
        } else {
            System.out.println("Status   : -");
        }
        System.out.printf("Total Ballots : %d%n", model.getTotalBallotCount());

        if (status == ElectionStatus.OPEN) {
            System.out.println("==================================");
            System.out.printf("Ballots Received : %d (Polls Open)%n", model.getTotalBallotCount());
            System.out.println("==================================");
        } else if (status == ElectionStatus.CLOSED) {
            System.out.println("==================================");
            System.out.println("[Status: Polls Closed / Pending Inspection]");
            ballotGroupsSummary(model.getAllBallotGroups());
            System.out.println("\n>> Provisional Scores (Certified Ballots Only):");
            scoresTable(model.calculateScores(), model.getAllCandidates());
            System.out.printf(" - Certified Ballots (Counted)    : %d%n", model.getCertifiedBallotCount());
            System.out.printf(" - Pending Inspection (Uncounted) : %d%n", model.getPendingBallotCount());
            System.out.printf(" - Void Ballots                   : %d%n", model.getVoidBallotCount());
            System.out.println("==================================");
        } else if (status == ElectionStatus.CONCLUDED) {
            System.out.println("==================================");
            System.out.println("[Status: Election Concluded (Final Result)]");
            System.out.println("\n>> Final Total Scores:");
            scoresTable(model.calculateScores(), model.getAllCandidates());
            System.out.println(">> Ballot Statistics:");
            System.out.printf(" - Certified Ballots (Counted) : %d%n", model.getCertifiedBallotCount());
            System.out.printf(" - Void Ballots (Uncounted)   : %d%n", model.getVoidBallotCount());
            System.out.printf(" - Total Ballots              : %d%n", model.getTotalBallotCount());
            System.out.println("==================================");
        }
    }

    public void ballotGroupsSummary(List<BallotGroup> groups) {
        System.out.println("\n[Duplicate Pattern Groups (>= 3 ballots)]:");
        if (groups == null || groups.isEmpty()) {
            System.out.println(" (No duplicate pattern groups found - all ballots certified)");
            return;
        }

        for (BallotGroup g : groups) {
            String statusNote;
            if (g.isPending()) {
                statusNote = ">> PENDING INSPECTION <<";
            } else {
                statusNote = "[" + g.getStatus() + "]";
            }
            System.out.printf(" * Group %s: Ranking [%s] -> %d ballots %s%n",
                    g.getGroupId(),
                    g.getRankingPatternKey(),
                    g.getBallotCount(),
                    statusNote);
            System.out.printf("   Ballot IDs: %s%n", g.getBallotIds());
        }
    }

    private void scoresTable(Map<String, Integer> scores, List<Candidate> candidates) {
        System.out.println("==================================");
        System.out.printf("ID          Name                            Score%n");
        System.out.println("==================================");
        for (Candidate c : candidates) {
            int score = scores.getOrDefault(c.getId(), 0);
            System.out.printf("%-10s  %-30s  %d pt%n", c.getId(), c.getName(), score);
        }
        System.out.println("==================================");
    }

    public void ballotsAudit(List<Ballot> ballots, List<Candidate> candidates) {
        System.out.println("\n==================================");
        System.out.println("Ballots Audit (Traceability)");
        System.out.println("==================================");
        System.out.printf("%-10s %-10s %-32s %-10s %s%n",
                "BallotID", "VoterID", "Ranking (1 -> 2 -> 3)", "GroupID", "Status");
        System.out.println("==================================");
        if (ballots == null || ballots.isEmpty()) {
            System.out.println("No ballots in system.");
        } else {
            for (Ballot b : ballots) {
                String group;
                if (b.getPatternGroupId() != null) {
                    group = b.getPatternGroupId();
                } else {
                    group = "-";
                }
                String rankingStr = b.getPatternKey();
                BallotStatus statusStr = b.getStatus();
                System.out.printf("%-10s %-10s %-32s %-10s %s%n",
                        b.getId(), b.getVoterId(), rankingStr, group, statusStr);
            }
        }
        System.out.println("==================================");
    }

    public void success(String message) {
        System.out.println("\n[SUCCESS] " + message);
    }

    public void error(String message) {
        System.out.println("\n[ERROR] " + message);
    }
}