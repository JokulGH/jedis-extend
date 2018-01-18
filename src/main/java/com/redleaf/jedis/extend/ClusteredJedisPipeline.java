package com.redleaf.jedis.extend;

import redis.clients.jedis.Client;
import redis.clients.jedis.PipelineBase;

import java.io.Closeable;
import java.io.IOException;

/**
 * 基于redis集群构建的pipeline操作，支持串行IO和并行IO两种操作模式<br/>
 * email:taojing.huang@qq.com <br/>
 * Created by Jokul on 2018/1/18.
 */
public class ClusteredJedisPipeline extends PipelineBase implements Closeable {
    @Override
    protected Client getClient(String key) {
        return null;
    }

    @Override
    protected Client getClient(byte[] key) {
        return null;
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
}
