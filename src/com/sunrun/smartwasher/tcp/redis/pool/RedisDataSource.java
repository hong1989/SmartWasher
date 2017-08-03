package com.sunrun.smartwasher.tcp.redis.pool;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import ch.qos.logback.classic.Logger;

/** 
 * @Package: com.sunrun.springmvc.redis 
 * @ClassName: RedisDataSource.java 
 * @Description:创建REDIS操作句柄
 * @author: HL 
 * @date: 创建时间：2016年5月16日 下午4:32:55
 * @version: 1.0 
 */

public class RedisDataSource {

	//logback打印日志，打印等级配置在logback.xml文件
	static Logger logger = (Logger) LoggerFactory.getLogger(RedisDataSource.class);  

	//redis线程池
	@Autowired
    private  ShardedJedisPool   pool;
    
	public RedisDataSource(ShardedJedisPool pool)
	{
		this.pool=pool;
	}

	//需加synchronized同步，否则多线程获取会出错
    public  synchronized ShardedJedis getRedisClient() 
    {
	  try {
            ShardedJedis shardJedis = pool.getResource();
            return shardJedis;
        } catch (Exception e) {
        	logger.error("getRedisClent error", e);
        }
		 
        return null;
    }

	public  void returnResource(ShardedJedis shardedJedis) {
    	//pool.returnResource(shardedJedis);
    	pool.returnResourceObject(shardedJedis);
    	
    }

    @SuppressWarnings("deprecation")
	public  void returnResource(ShardedJedis shardedJedis, boolean broken) {
        if (broken) {
        	pool.returnBrokenResource(shardedJedis);
        	//pool.returnResourceObject(shardedJedis);
        } else {
        	pool.returnResource(shardedJedis);
        	//pool.returnResourceObject(shardedJedis);
        }
    	//pool.returnResourceObject(shardedJedis);
    }
}