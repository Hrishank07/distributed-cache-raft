package com.example.raftcache.api;

import com.example.raftcache.config.NodeConfig;
import com.example.raftcache.raft.RaftState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AdminController {

    private final NodeConfig nodeConfig;
    private final RaftState raftState;

    public AdminController(NodeConfig nodeConfig, RaftState raftState) {
        this.nodeConfig = nodeConfig;
        this.raftState = raftState;
    }

    @GetMapping("/raft/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("nodeId", raftState.getNodeId());
        out.put("role", raftState.getRole().name());
        out.put("currentTerm", raftState.getCurrentTerm());
        out.put("leaderId", raftState.getLeaderId());
        out.put("votedFor", raftState.getVotedFor());
        out.put("clusterNodes", nodeConfig.getClusterNodes());
        return ResponseEntity.ok(out);
    }
}
