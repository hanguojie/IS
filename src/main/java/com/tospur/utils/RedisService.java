package com.tospur.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    
    private static String redisCode = "utf-8";

    /**
     * @param key
     */
    public long del(final String... keys) {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(final byte[] key, final byte[] value, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key, value);
                if (liveTime > 0) {
                    connection.expire(key, liveTime);
                }
                return 1L;
            }
        });
    }

    /**
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, long liveTime) {
        this.set(key.getBytes(), value.getBytes(), liveTime);
    }

    /**
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        this.set(key, value, 0L);
    }
    
    /**
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        this.set(key, value, 0L);
    }

    /**
     * @param key
     * @return
     */
    public String get(final String key) {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                	if(connection.get(key.getBytes())==null || connection.get(key.getBytes()).equals("")){
                		return "";
                	}
                    return new String(connection.get(key.getBytes()), redisCode);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return "";
            }
        });
    }

    /**
     * @param pattern
     * @return 
     * @return
     */
    public Set<String> Setkeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return (boolean) redisTemplate.execute((RedisCallback) connection -> connection.exists(key.getBytes()));
    }

    /**
     * @return
     */
    public String flushDB() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    /**
     * @return
     */
    public long dbSize() {
        return (long) redisTemplate.execute(new RedisCallback() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
    }

    /**
     * @return
     */
    public String ping() {
        return (String) redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {

                return connection.ping();
            }
        });
    }
    
    private RedisService() {

    }
    
    
    /**
     * 获取MAP
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> getMap(final String key){
    	return (Map<String, Object>) redisTemplate.execute(new RedisCallback() {
            @SuppressWarnings("unused")
			public Map<String,Object> doInRedis(RedisConnection connection) throws DataAccessException {
                	 byte[] keyByte = redisTemplate.getStringSerializer().serialize(key);
                	 if(connection.exists(keyByte)){
                		 Map<byte[], byte[]> hGetAll = connection.hGetAll(keyByte);
                		 
                		 Map<String,Object> dataMap = new HashMap<String,Object>();
                		 for (byte[] k : hGetAll.keySet()) {
                			 byte[] value = hGetAll.get(k);
                			 Object valueObj = redisTemplate.getDefaultSerializer().deserialize(value);
                			 String keyStr = redisTemplate.getStringSerializer().deserialize(k);
                			 dataMap.put(keyStr, valueObj);
 						 }
                		 return dataMap;
                	 }
                	 return  null;
            }
        });
    }
    
}