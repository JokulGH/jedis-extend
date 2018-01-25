package com.redleaf.jedis.extend;

import junit.framework.TestCase;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jokul on 2018/1/22.
 */
public class ClusteredJedisPipelineTest extends TestCase {

    @Test
    public void testSyncAndReturnAll(){

        Set<HostAndPort> hosts = new HashSet<HostAndPort>();
        hosts.add(new HostAndPort("192.168.125.13", 6379));
        hosts.add(new HostAndPort("192.168.125.13", 6380));

        ClusteredJedis jedis = new ClusteredJedis(hosts);
        ClusteredJedisPipeline pipeline = jedis.pipelined();

        try {
            while (true) {
//            long startTime = System.nanoTime();
                long startTime1 = System.currentTimeMillis();

                for (int i = 0; i < 10000; i++) {
                    pipeline.set("key" + i, "value" + i);
                }
                pipeline.sync();
//            System.out.println(System.nanoTime() - startTime);
//            System.out.println(System.currentTimeMillis() - startTime1);

                for (int i = 0; i < 10000; i++) {
                    pipeline.get("key" + i);
                }
                List<Object> results = pipeline.syncAndReturnAll();

//            for (Object obj : results){
//                System.out.println(obj.toString());
//            }

//            System.out.println(System.nanoTime() - startTime );
                System.out.println(System.currentTimeMillis() - startTime1);
                System.out.println(results.size());

                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                pipeline.close();
                jedis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
