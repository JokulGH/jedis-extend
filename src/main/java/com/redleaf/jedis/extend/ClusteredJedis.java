package com.redleaf.jedis.extend;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * 对jedis中的JedisCluster的包装类，为了ClusteredJedisPipeline 提供服务，提供Jedis.pipelined()类似的功能<br/>
 * email:taojing.huang@qq.com <br/>
 * Created by Jokul on 2018/1/18.
 */
public class ClusteredJedis extends JedisCluster {


    public ClusteredJedis(HostAndPort node) {
        super(node);
    }

    public ClusteredJedis(HostAndPort node, int timeout) {
        super(node, timeout);
    }

    public ClusteredJedis(HostAndPort node, int timeout, int maxAttempts) {
        super(node, timeout, maxAttempts);
    }

    public ClusteredJedis(HostAndPort node, GenericObjectPoolConfig poolConfig) {
        super(node, poolConfig);
    }

    public ClusteredJedis(HostAndPort node, int timeout, GenericObjectPoolConfig poolConfig) {
        super(node, timeout, poolConfig);
    }

    public ClusteredJedis(HostAndPort node, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(node, timeout, maxAttempts, poolConfig);
    }

    public ClusteredJedis(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(node, connectionTimeout, soTimeout, maxAttempts, poolConfig);
    }

    public ClusteredJedis(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        super(node, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    }

    public ClusteredJedis(Set<HostAndPort> nodes) {
        super(nodes);
    }

    public ClusteredJedis(Set<HostAndPort> nodes, int timeout) {
        super(nodes, timeout);
    }

    public ClusteredJedis(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
        super(nodes, timeout, maxAttempts);
    }

    public ClusteredJedis(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig) {
        super(nodes, poolConfig);
    }

    public ClusteredJedis(Set<HostAndPort> nodes, int timeout, GenericObjectPoolConfig poolConfig) {
        super(nodes, timeout, poolConfig);
    }

    public ClusteredJedis(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, maxAttempts, poolConfig);
    }

    public ClusteredJedis(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
    }

    public ClusteredJedis(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    }

//    public Pipeline pipelined() {
//        pipeline = new Pipeline();
//        pipeline.setClient(client);
//        return pipeline;
//    }
}
