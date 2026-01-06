# Distributed Cache with Raft Consensus (Java + Spring Boot)

A learning-focused implementation of a **strongly consistent distributed cache** built on the **Raft consensus algorithm**. The goal is to understand how real systems keep multiple nodes in agreement under failures (leader crashes, partitions), not to build a Redis replacement.

---

## Why this exists

Single-node caches are fast but fragile. As soon as you run multiple cache nodes, you hit hard distributed problems:
- Who decides the “official” order of writes?
- What happens when the leader dies mid-request?
- How do replicas recover without diverging?
- How do you stay correct under network partitions?

This project answers those questions by implementing:
- **Leader election**
- **Log replication**
- **Majority quorum commits**
- **Deterministic failure behavior**

This mirrors the core mechanics used in systems like coordination stores and control planes (e.g., etcd/Consul-style designs).

---

## Goals

### Primary goal
Implement a **strongly consistent** replicated cache across multiple nodes using **Raft**:
- One elected leader per term
- All writes ordered by the leader
- Writes succeed only after **majority replication**
- All nodes apply committed writes in the same order

### Secondary goals
- Demonstrate automatic failover when the leader crashes
- Provide clear observability into Raft state (role, term, leader, commit index)
- Provide a repeatable way to simulate failures using Docker

---

## Non-goals (v1)

To keep the project focused and credible:
- No sharding / consistent hashing
- No dynamic membership changes (nodes are static)
- No multi-key transactions
- No TTL / eviction policies
- No attempt to outperform production caches/databases

---

## Consistency & Failure Semantics

### Consistency model
- **Writes are linearizable** (leader-ordered + quorum commit)
- Reads are served from each node’s **committed state**

### Failure behavior (expected)
| Scenario | Expected behavior |
|---------|-------------------|
| Leader crashes | New leader is elected automatically |
| Follower crashes | Cluster remains writable (as long as majority is alive) |
| Network partition | Majority side remains writable; minority side rejects writes |
| Slow/delayed messages | Term/role rules prevent stale leaders from committing |

This is an explicit choice: **consistency over availability** during partitions.

---

## High-level Architecture

- Cluster of `N` nodes (start with **3**)
- Each node runs:
  - **Raft module** (election + replication)
  - **Replicated log** (commands like SET/DEL)
  - **State machine** (cache)
  - REST APIs:
    - client APIs (GET/PUT/DELETE)
    - internal Raft RPCs (RequestVote, AppendEntries)
    - admin/status APIs

### Raft RPCs implemented
- `RequestVote` (leader election)
- `AppendEntries` (heartbeats + log replication)

---

## API (Planned)

### Client APIs
- `GET /cache/{key}`
- `PUT /cache/{key}` body: `{ "value": "..." }`
- `DELETE /cache/{key}`

Notes:
- Only the **leader** accepts writes.
- Followers will return a redirect (or error with leader info) for writes.

### Admin APIs
- `GET /raft/status`  
  Returns: nodeId, role, currentTerm, leaderId, commitIndex, lastApplied, logLength

---

## Tech Stack

- Java 17
- Spring Boot (Web + Actuator)
- Docker + Docker Compose
- (Optional later) Redis-per-node for the state machine, metrics via Prometheus/Grafana

---

## Roadmap (Implementation Phases)

### Phase 0: Project skeleton
- Spring Boot app + health endpoint
- Docker Compose runs 3 nodes

### Phase 1: Raft state + election timer
- Node roles (Follower/Candidate/Leader)
- Randomized election timeout
- Candidate transition on timeout

### Phase 2: RequestVote + leader election
- Majority vote -> leader
- Term handling, vote rules

### Phase 3: Heartbeats (AppendEntries empty)
- Leader heartbeats prevent elections
- Follower resets election timer on valid heartbeats

### Phase 4: Log replication + commit
- Leader appends SET/DEL into log
- Replicate to followers via AppendEntries
- Commit on majority
- Apply committed entries to cache state machine

### Phase 5: Failure simulation
- Kill leader and verify failover
- Kill follower and verify continued writes
- Partition tests (majority continues, minority rejects)

---

## How to Run (Coming soon)

This repo will include:
- `docker/docker-compose.yml` to start a 3-node cluster
- scripts in `scripts/` to simulate failures (kill leader, partition node)
- load test scripts to measure basic throughput

---

## Project Status
Early development — skeleton + design stabilized, implementation in progress.

---