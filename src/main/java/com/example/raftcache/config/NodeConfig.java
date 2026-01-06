package com.example.raftcache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NodeConfig {

    // Example: node1, node2, node3
    private final String nodeId;

    // Example: http://node1:8080,http://node2:8080,http://node3:8080
    private final String clusterNodes;

    public NodeConfig(
            @Value("${node.id:node1}") String nodeId,
            @Value("${cluster.nodes:}") String clusterNodes
    ) {
        this.nodeId = nodeId;
        this.clusterNodes = clusterNodes;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }
}
