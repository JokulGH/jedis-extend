package com.redleaf.jedis.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;
import redis.clients.util.SafeEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 基于redis集群构建的pipeline操作，支持串行IO和并行IO两种操作模式<br/>
 * email:taojing.huang@qq.com <br/>
 * Created by Jokul on 2018/1/18.
 */
public class ClusteredJedisPipeline extends PipelineBase implements Closeable {

    private static final String CONNECTIONHANDLER_FIELD = "cache";
    private static final Logger logger = LoggerFactory.getLogger(ClusteredJedisPipeline.class);

    private JedisSlotBasedConnectionHandler connectionHandler;
    private JedisClusterInfoCache clusterInfoCache;
    private Queue<Client> clients = new LinkedList<Client>();
    private Map<JedisPool, Jedis> jedisCache = new HashMap<JedisPool, Jedis>();
    private Map<Client, Queue<Response<?>>> clientQueueMap; //保存每个客户端对应的返回值队列

    public ClusteredJedisPipeline(JedisSlotBasedConnectionHandler connectionHandler){
        this.connectionHandler = connectionHandler;
        this.clusterInfoCache = getClusterInfo(this.connectionHandler);
    }

    /**
     * Syncronize pipeline by reading all responses. This operation closes the pipeline. In order to
     * get return values from pipelined commands, capture the different Response&lt;?&gt; of the
     * commands you execute.
     */
    public void sync() {
        for (Client client : clients) {
            generateResponse(client.getOne());
        }
    }

    /**
     * Syncronize pipeline by reading all responses. This operation closes the pipeline. Whenever
     * possible try to avoid using this version and use ShardedJedisPipeline.sync() as it won't go
     * through all the responses and generate the right response type (usually it is a waste of time).
     * @return A list of all the responses in the order you executed them.
     */
    public List<Object> syncAndReturnAll() {
        List<Object> formatted = new ArrayList<Object>();
        for (Client client : clients) {
            formatted.add(generateResponse(client.getOne()).get());
        }
        return formatted;
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    public void close() throws IOException {

    }

    @Override
    protected Client getClient(String key) {
        Client client = getClient(SafeEncoder.encode(key));
        return client;
    }

    @Override
    protected Client getClient(byte[] key) {

        Jedis jedis = getJedis(key);
        Client client = jedis.getClient();
        clients.add(client);
        return client;
    }

    @Override
    protected <T> Response<T> getResponse(Builder<T> builder) {
        if (clientQueueMap == null || clientQueueMap.isEmpty()){
            clientQueueMap = new HashMap<Client, Queue<Response<?>>>();
        }
//        clientQueueMap.get(get)
        return super.getResponse(builder);
    }

    /**
     * 通过反射获取集群信息缓存，主要用于通过key获取对应的连接池及连接对象
     * @param connectionHandler
     * @return
     */
    private JedisClusterInfoCache getClusterInfo(JedisClusterConnectionHandler connectionHandler){

        try {
            Field field = JedisClusterConnectionHandler.class.getDeclaredField(CONNECTIONHANDLER_FIELD);
            field.setAccessible(true);
            return  (JedisClusterInfoCache) field.get(connectionHandler);
        }catch (Exception e){
            logger.error("获取集群缓存信息出错", e);
        }
        return null;
    }

    /**
     * 通过key获取key对应的Slot使用的连接池，并从池中获取一个jedis<br/>
     * 采用本地缓存将获取到jedis进行缓存，在同一次pipeline操作中必须使用相同的client进行操作
     * @param key
     * @return
     */
    private Jedis getJedis(byte[] key){
        JedisPool pool = clusterInfoCache.getSlotPool(JedisClusterCRC16.getSlot(key));
        if (pool == null){  //这里是否需要刷新集群信息？
            logger.warn("通过key {} 获取不到对应的连接池对象", SafeEncoder.encode(key));
            connectionHandler.renewSlotCache();
            pool = clusterInfoCache.getSlotPool(JedisClusterCRC16.getSlot(key));
        }

        Jedis jedis = jedisCache.get(pool);
        if (jedis == null){
            jedis = pool.getResource();
        }

        return jedis;
    }
}
