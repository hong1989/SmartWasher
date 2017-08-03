package com.sunrun.smartwasher.tcp.redis.dao.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sunrun.smartwasher.tcp.redis.dao.RedisDao;
import com.sunrun.smartwasher.tcp.redis.pool.RedisDataSource;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import ch.qos.logback.classic.Logger;

/** 
 * @Package: com.sunrun.bing.redis.dao.impl 
 * @ClassName: RedisDaoImpl.java 
 * @Description:
 * @author: HL 
 * @date: 创建时间：2016年8月17日 上午9:05:58
 * @version: 1.0 
 */
@Repository
public class RedisDaoImpl implements RedisDao {

	private static Logger logger = (Logger) LoggerFactory.getLogger(RedisDaoImpl.class); 
	
	@Autowired
	private RedisDataSource redisDataSource;

	/**
	* 覆盖父类方法
	* Title: set
	* Description: Redis SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。
	* @param key
	* @param value
	* @return  成功返回OK
	* @see com.sunrun.RedisDao.redis.dao.RedisCacheDao#set(java.lang.String, java.lang.String)
	 */
	@Override
	public String set(String key, String value) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
            shardedJedis.expire(key, 120);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	/**
	* 覆盖父类方法
	* Title: set
	* Description: 
	* @param shardedJedis
	* @param key  设备唯一标示符作为key
	* @param value 
	* @return  
	* @see com.sunrun.tcp.redis.dao.RedisDao#set(redis.clients.jedis.ShardedJedis, java.lang.String, java.lang.String)
	 */
	@Override
	public String set(ShardedJedis shardedJedis,String key, String value)
	{
		String result = null;
		boolean broken = false;
		try {
			result = shardedJedis.set(key, value);//缓存
			shardedJedis.expire(key, 120);//保留120秒
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
			broken = true;
	        redisDataSource.returnResource(shardedJedis, broken);
		}
		return result;
	}
	
	/**
	* 覆盖父类方法
	* Title: get
	* Description: Redis Get 命令用于获取指定 key 的值。
	* @param key
	* @return  成功返回key对应的value值，如果 key 不存在，返回 nil，如果key 储存的值不是字符串类型，返回一个错误
	* @see com.sunrun.RedisDao.redis.dao.RedisCacheDao#get(java.lang.String)
	 */
	@Override
	public String get(String key) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.get(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	/**
	* 覆盖父类方法
	* Title: get
	* Description: 
	* @param shardedJedis
	* @param key
	* @return  
	* @see com.sunrun.tcp.redis.dao.RedisDao#get(redis.clients.jedis.ShardedJedis, java.lang.String)
	 */
	@Override
	public String get(ShardedJedis shardedJedis,String key){
		
		String result = null;
	    boolean broken = false;
        try {
            result = shardedJedis.get(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
            redisDataSource.returnResource(shardedJedis, broken);
        } finally {
           
        }
        return result;
	}
	/**
	* 覆盖父类方法
	* Title: exists
	* Description: 
	* @param key
	* @return  
	* @see com.sunrun.RedisDao.redis.dao.RedisCacheDao#exists(java.lang.String)
	 */
	@Override
	public Boolean exists(String key) {
		
		Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.exists(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String type(String key) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.type(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expire(key, seconds);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long expireAt(String key, long unixTime) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expireAt(key, unixTime);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long ttl(String key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ttl(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public boolean setbit(String key, long offset, boolean value) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        boolean result = false;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setbit(key, offset, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public boolean getbit(String key, long offset) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        boolean result = false;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;

        try {
            result = shardedJedis.getbit(key, offset);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public long setrange(String key, long offset, String value) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        long result = 0;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setrange(key, offset, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String getrange(String key, long startOffset, long endOffset) {
		
		 ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	        String result = null;
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {
	            result = shardedJedis.getrange(key, startOffset, endOffset);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            redisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	}

	@Override
	public String getSet(String key, String value) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	/**
	* 覆盖父类方法
	* Title: setnx
	* Description: - 1，当 key 的值被设置 
				   - 0，当 key 的值没被设置
	* @param key
	* @param value
	* @return  
	* @see com.sunrun.tcp.redis.dao.RedisDao#setnx(java.lang.String, java.lang.String)
	 */
	@Override
	public Long setnx(String key, String value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String setex(String key, int seconds, String value) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setex(key, seconds, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long decrBy(String key, long integer) {
		
	    Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long decr(String key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long incrBy(String key, long integer) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long incr(String key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long append(String key, String value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.append(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String substr(String key, int start, int end) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.substr(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hset(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String hget(String key, String field) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hget(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hsetnx(String key, String field, String value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hsetnx(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	/**
	* 覆盖父类方法
	* Title: hmset
	* Description: Redis Hmset 命令用于同时将多个 field-value (字段-值)对设置到哈希表中
	* @param key
	* @param hash
	* @return  成功返回：OK 其他返回表示出错
	* @see com.sunrun.RedisDao.redis.dao.RedisCacheDao#hmset(java.lang.String, java.util.Map)
	 */
	@Override
	public String hmset(String key, Map<String, String> hash) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmset(key, hash);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}
	
	/**
	* 覆盖父类方法
	* Title: hmset
	* Description: Redis Hmset 命令用于同时将多个 field-value (字段-值)对设置到哈希表中,这边利用管道异步插入redis，增加效率
	* @param redisHashMap
	* @return  
	* @see com.sunrun.RedisDao.redis.dao.RedisCacheDao#hmset(java.util.HashMap)
	 */
	@Override
    public List<Object> hmset(ShardedJedis shardedJedis,HashMap<String, HashMap<String, String>> redisHashMap)
	{
		List<Object> result = null;
        boolean broken = false;
        try {
        	ShardedJedisPipeline pipeline=shardedJedis.pipelined();
            Iterator<Map.Entry<String,HashMap<String,String>>> ite_redis = redisHashMap.entrySet().iterator();
    		while (ite_redis.hasNext()) {
    			Map.Entry<String,HashMap<String,String>> mapentry_redis =ite_redis.next();
    			String key =  mapentry_redis.getKey().toString();
    			HashMap<String,String> redisHash = mapentry_redis.getValue();
    			pipeline.hmset(key, redisHash);
    			pipeline.expire(key, 60);//设置key过期时间1分钟

    		}    	
    		result=pipeline.syncAndReturnAll();
    		
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
            redisDataSource.returnResource(shardedJedis, broken);
        } finally {
        	
        }
        return result;	
    }

	@Override
	public List<String> hmget(String key, String... fields) {
		
		List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmget(key, fields);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hincrBy(String key, String field, long value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hincrBy(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Boolean hexists(String key, String field) {
		
		Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hexists(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long del(String key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.del(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hdel(String key, String field) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hlen(String key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hlen(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> keys(String key) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
        	Jedis j = shardedJedis.getShard(key);
        	result = j.keys(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> hkeys(String key) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hkeys(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public List<String> hvals(String key) {
		
		List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hvals(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		
		Map<String, String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hgetAll(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long rpush(String key, String string) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
        	
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpush(key, string);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long lpush(String key, String string) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpush(key, string);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long llen(String key) {
		
		Long result = (long) 0;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.llen(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long llen(ShardedJedis shardedJedis,String key) {
		
		Long result = (long) 0;
        boolean broken = false;
        try {
            result = shardedJedis.llen(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
            redisDataSource.returnResource(shardedJedis, broken); 
        } finally {
        	
        }
        return result;
	}
	
	@Override
	public List<String> lrange(String key, long start, long end) {
		
		List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrange(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String ltrim(String key, long start, long end) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ltrim(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String lindex(String key, long index) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lindex(key, index);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String lset(String key, long index, String value) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lset(key, index, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long lrem(String key, long count, String value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrem(key, count, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String lpop(String key) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpop(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}
	
	@Override
	public String lpop(ShardedJedis shardedJedis,String key) {
		
		String result = null;
        boolean broken = false;
        try {
            result = shardedJedis.lpop(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
            redisDataSource.returnResource(shardedJedis, broken);
        } finally {
        	
        }
        return result;
	}

	@Override
	public String rpop(String key) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpop(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long sadd(String key, String member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sadd(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> smembers(String key) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.smembers(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long srem(String key, String member) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Long result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srem(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String spop(String key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.spop(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long scard(String key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Long result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.scard(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Boolean sismember(String key, String member) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Boolean result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sismember(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
}

	@Override
	public String srandmember(String key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srandmember(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zadd(String key, double score, String member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zadd(key, score, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> zrange(String key, int start, int end) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zrem(String key, String member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrem(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zpop(String key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
			String member=null;
			Set<String> idSet=shardedJedis.zrange(key, 0, 0);
			if(idSet.size()>0)
			{
				member=(String)idSet.toArray()[0];
			}
        	if(member!=null)
        	{
        		result = shardedJedis.zrem(key, member);
        	}
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Double zincrby(String key, double score, String member) {
		
		Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zincrby(key, score, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zrank(String key, String member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrank(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zrevrank(String key, String member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrank(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> zrevrange(String key, int start, int end) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrange(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeWithScores(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeWithScores(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zcard(String key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcard(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Double zscore(String key, String member) {
		
		Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zscore(key, member);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public List<String> sort(String key) {
		
		List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public List<String> sort(String key, SortingParams sortingParameters) {
		
		List<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key, sortingParameters);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zcount(String key, double min, double max) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcount(key, min, max);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> zrangeByScore(String key, double min, double max,
			int offset, int count) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<String> zrevrangeByScore(String key, double max, double min,
			int offset, int count) {
		
		Set<String> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min) {
		
		 Set<Tuple> result = null;
	        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
	        if (shardedJedis == null) {
	            return result;
	        }
	        boolean broken = false;
	        try {

	            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            broken = true;
	        } finally {
	            redisDataSource.returnResource(shardedJedis, broken);
	        }
	        return result;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(String key, double min,
			double max, int offset, int count) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min, int offset, int count) {
		
	    Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zremrangeByRank(String key, int start, int end) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByRank(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zremrangeByScore(String key, double start, double end) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByScore(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.linsert(key, where, pivot, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String set(byte[] key, byte[] value) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.set(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] get(byte[] key) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.get(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Boolean exists(byte[] key) {
		
		Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.exists(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String type(byte[] key) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.type(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long expire(byte[] key, int seconds) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.expire(key, seconds);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long expireAt(byte[] key, long unixTime) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.expireAt(key, unixTime);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long ttl(byte[] key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.ttl(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] getSet(byte[] key, byte[] value) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.getSet(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long setnx(byte[] key, byte[] value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.setnx(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String setex(byte[] key, int seconds, byte[] value) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.setex(key, seconds, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long decrBy(byte[] key, long integer) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.decrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long decr(byte[] key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.decr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long incrBy(byte[] key, long integer) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.incrBy(key, integer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long incr(byte[] key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.incr(key);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long append(byte[] key, byte[] value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.append(key, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] substr(byte[] key, int start, int end) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.substr(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hset(byte[] key, byte[] field, byte[] value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hset(key, field, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hget(key, field);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hsetnx(key, field, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hmset(key, hash);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		
		List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hmget(key, fields);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hincrBy(byte[] key, byte[] field, long value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hincrBy(key, field, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Boolean hexists(byte[] key, byte[] field) {
		
		Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hexists(key, field);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hdel(byte[] key, byte[] field) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hdel(key, field);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long hlen(byte[] key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hlen(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> hkeys(byte[] key) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hkeys(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Collection<byte[]> hvals(byte[] key) {
		
		Collection<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hvals(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		
		Map<byte[], byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.hgetAll(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long rpush(byte[] key, byte[] string) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.rpush(key, string);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long lpush(byte[] key, byte[] string) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lpush(key, string);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long llen(byte[] key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.llen(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public List<byte[]> lrange(byte[] key, int start, int end) {
		
		List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lrange(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String ltrim(byte[] key, int start, int end) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.ltrim(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] lindex(byte[] key, int index) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lindex(key, index);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String lset(byte[] key, int index, byte[] value) {
		
		String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lset(key, index, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long lrem(byte[] key, int count, byte[] value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lrem(key, count, value);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] lpop(byte[] key) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.lpop(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] rpop(byte[] key) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.rpop(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long sadd(byte[] key, byte[] member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sadd(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> smembers(byte[] key) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.smembers(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long srem(byte[] key, byte[] member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.srem(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] spop(byte[] key) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.spop(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long scard(byte[] key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.scard(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Boolean sismember(byte[] key, byte[] member) {
		
		Boolean result = false;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sismember(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public byte[] srandmember(byte[] key) {
		
		byte[] result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.srandmember(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zadd(byte[] key, double score, byte[] member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zadd(key, score, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> zrange(byte[] key, int start, int end) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrange(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zrem(byte[] key, byte[] member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrem(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Double zincrby(byte[] key, double score, byte[] member) {
		
		Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zincrby(key, score, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zrank(byte[] key, byte[] member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrank(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zrevrank(byte[] key, byte[] member) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrank(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrange(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeWithScores(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeWithScores(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zcard(byte[] key) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcard(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Double zscore(byte[] key, byte[] member) {
		
		Double result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zscore(key, member);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public List<byte[]> sort(byte[] key) {
		
		List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		
		List<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.sort(key, sortingParameters);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zcount(byte[] key, double min, double max) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zcount(key, min, max);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> zrangeByScore(byte[] key, double min, double max,
			int offset, int count) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScore(key, min, max, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min,
			double max, int offset, int count) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min,
			int offset, int count) {
		
		Set<byte[]> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min, int offset, int count) {
		
		Set<Tuple> result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zremrangeByRank(byte[] key, int start, int end) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByRank(key, start, end);

        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long zremrangeByScore(byte[] key, double start, double end) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.zremrangeByScore(key, start, end);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot,
			byte[] value) {
		
		Long result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.linsert(key, where, pivot, value);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        List<Object> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pipelined(shardedJedisPipeline);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Jedis getShard(byte[] key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Jedis getShard(String key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public JedisShardInfo getShardInfo(byte[] key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public JedisShardInfo getShardInfo(String key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public String getKeyTag(String key) {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getKeyTag(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Collection<JedisShardInfo> getAllShardInfo() {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Collection<JedisShardInfo> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getAllShardInfo();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}

	@Override
	public Collection<Jedis> getAllShards() {
		
		ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        Collection<Jedis> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getAllShards();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis, broken);
        }
        return result;
	}	

}
