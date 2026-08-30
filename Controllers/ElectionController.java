package Controllers;

import Models.*;
import Views.ElectionView;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ElectionController {
    private final ElectionService model;
    private final ElectionView view;
    private final Scanner scanner;

    public ElectionController(ElectionService model, ElectionView view) {
        this.model = model;
        this.view = view;
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public void start() {
        view.title();

        boolean running = true;
        while (running) {
            view.mainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showCandidates();
                    break;
                case "2":
                    processVoteInput();
                    break;
                case "3":
                    handleClosePolls();
                    break;
                case "4":
                    processGroupDecision();
                    break;
                case "5":
                    showStatus();
                    break;
                case "6":
                    showBallotsAudit();
                    break;
                case "0":
                    view.exit();
                    running = false;
                    break;
                default:
                    view.error("Invalid, enter 0-6.");
            }
        }
    }

    public void showCandidates() {
        view.candidates(model.getAllCandidates());
    }

    public void showStatus() {
        view.summary(model);
    }

    public void showBallotsAudit() {
        view.ballotsAudit(model.getAllBallots(), model.getAllCandidates());
    }

    private void processVoteInput() {
        view.votingTitle();
        System.out.print("Enter Voter ID: ");
        String voterId = scanner.nextLine().trim();

        System.out.print("Enter Candidate ID for Rank 1: ");
        String rank1 = scanner.nextLine().trim();

        System.out.print("Enter Candidate ID for Rank 2: ");
        String rank2 = scanner.nextLine().trim();

        System.out.print("Enter Candidate ID for Rank 3: ");
        String rank3 = scanner.nextLine().trim();

        List<String> ranking = Arrays.asList(rank1, rank2, rank3);
        handleCastVote(voterId, ranking);
    }

    public void handleCastVote(String voterId, List<String> rankedCandidateIds) {
        try {
            Ballot ballot = model.castVote(voterId, rankedCandidateIds);
            view.success("Vote casted. Ballot ID: " + ballot.getId());
        } catch (Exception e) {
            view.error(e.getMessage());
        }
    }

    public void handleClosePolls() {
        try {
            model.closePollsAndDetectPatterns();
            view.success("Polls closed.");
            view.summary(model);
        } catch (Exception e) {
            view.error(e.getMessage());
        }
    }

    private void processGroupDecision() {
        List<BallotGroup> pendingGroups = model.getPendingBallotGroups();
        if (pendingGroups.isEmpty()) {
            if (model.getStatus() == ElectionStatus.OPEN) {
                view.error("Election is still open.");
            } else if (model.getStatus() == ElectionStatus.CONCLUDED) {
                view.error("Election is already concluded.");
            } else {
                view.success("No more pending duplicate group.");
            }
            return;
        }

        view.votingAudit();
        view.ballotGroupsSummary(pendingGroups);

        System.out.print("\nEnter Group ID: ");
        String groupId = scanner.nextLine().trim();

        BallotGroup group = model.getBallotGroup(groupId);
        if (group == null) {
            view.error("Group ID not found: " + groupId);
            return;
        }

        if (!group.isPending()) {
            view.error("Group " + groupId + " is already decided (" + group.getStatus() + ").");
            return;
        }

        view.votingAuditMenu(groupId, group.getBallotCount());
        String decision = scanner.nextLine().trim();

        if ("1".equals(decision)) {
            handleDecideGroup(groupId, true);
        } else if ("2".equals(decision)) {
            handleDecideGroup(groupId, false);
        } else {
            view.error("Invalid, select 1 or 2.");
        }
    }

    public void handleDecideGroup(String groupId, boolean certify) {
        try {
            model.decidePatternGroup(groupId, certify);
            if (certify) {
                view.success("Recorded decision for group " + groupId + " as: " + "CERTIFIED");
            } else {
                view.success("Recorded decision for group " + groupId + " as: " + "VOID");
            }
            view.summary(model);
        } catch (Exception e) {
            view.error(e.getMessage());
        }
    }
}