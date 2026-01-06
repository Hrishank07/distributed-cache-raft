package com.example.raftcache.raft;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Holds the local Raft state for this node.
 * v1: in-memory only; persistence comes later.
 */
public class RaftState {

    private final String nodeId;

    // Raft persistent state (v1 in-memory)
    private final AtomicInteger currentTerm = new AtomicInteger(0);
    private final AtomicReference<String> votedFor = new AtomicReference<>(null);

    // Volatile state
    private final AtomicReference<RaftNodeRole> role = new AtomicReference<>(RaftNodeRole.FOLLOWER);
    private final AtomicReference<String> leaderId = new AtomicReference<>(null);

    public RaftState(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public int getCurrentTerm() {
        return currentTerm.get();
    }

    public void setCurrentTerm(int term) {
        currentTerm.set(term);
    }

    public String getVotedFor() {
        return votedFor.get();
    }

    public void setVotedFor(String nodeId) {
        votedFor.set(nodeId);
    }

    public RaftNodeRole getRole() {
        return role.get();
    }

    public void setRole(RaftNodeRole newRole) {
        role.set(newRole);
    }

    public String getLeaderId() {
        return leaderId.get();
    }

    public void setLeaderId(String leaderNodeId) {
        leaderId.set(leaderNodeId);
    }
}
