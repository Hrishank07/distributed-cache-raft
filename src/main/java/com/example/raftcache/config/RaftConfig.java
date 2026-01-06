package com.example.raftcache.config;

import com.example.raftcache.raft.RaftState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RaftConfig {

    @Bean
    public RaftState raftState(NodeConfig nodeConfig) {
        return new RaftState(nodeConfig.getNodeId());
    }
}
