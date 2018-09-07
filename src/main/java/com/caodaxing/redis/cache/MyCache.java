package com.caodaxing.redis.cache;

import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.caodaxing.redis.utils.SerializableUtil;

@Component
public class MyCache implements Cache {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	private String name;
	
	private long timeout;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this.redisTemplate;
	}

	@Override
	public ValueWrapper get(Object key) {
		if(key == null || StringUtils.isEmpty(key.toString())) {
			return null;
		}else {
			final String finalKey = formatStr(key);
			redisTemplate.multi();
			Object object = redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] newKey = finalKey.getBytes();
					byte[] value = connection.get(newKey);
					if(value == null) {
						return null;
					}
					return SerializableUtil.unserializeList(value);
				}
			});
			redisTemplate.exec();
			return object != null?new SimpleValueWrapper(object):null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		if(key == null || type == null) {
			return null;
		}else {
			final String finalKey = formatStr(key);
			Object obj = redisTemplate.execute(new RedisCallback<Object>() {
				
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] newKey = finalKey.getBytes();
					byte[] value = connection.get(newKey);
					if(value == null) {
						return null;
					}
					return SerializableUtil.unserializeList(value);
				}
			});
			if(obj == null) {
				return null;
			}
			return (T) obj;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		if(key == null || StringUtils.isEmpty(key.toString())) {
			return null;
		}else {
			final String finalKey = formatStr(key);
			
			Object object = redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					byte[] newKey = finalKey.getBytes();
					byte[] value = connection.get(newKey);
					if(value == null) {
						return null;
					}
					return SerializableUtil.unserializeList(value);
				}
			});
			return (T) (object != null?new SimpleValueWrapper(object):null);
		}
	}

	@Override
	public void put(Object key, Object value) {
		if (key==null || value==null) {
            return;
        } else {
            final String finalKey = formatStr(key);
            if (!StringUtils.isEmpty(finalKey)) {
                final Object finalValue = value;
                redisTemplate.multi();
                redisTemplate.execute(new RedisCallback<Boolean>() {
                    @Override
                    public Boolean doInRedis(RedisConnection connection) {
                        connection.set(finalKey.getBytes(), SerializableUtil.serialize(finalValue));
                        // 设置超时间
                        connection.expire(finalKey.getBytes(), timeout);
                        return true;
                    }
                });
                redisTemplate.exec();
            }
        }
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		if (key==null || value==null) {
            return null;
        } else {
            final String finalKey = formatStr(key);
            Object obj = null;
            if (!StringUtils.isEmpty(finalKey)) {
                final Object finalValue = value;
                obj = redisTemplate.execute(new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(RedisConnection connection) {
                        connection.set(finalKey.getBytes(), SerializableUtil.serialize(finalValue));
                        // 设置超时间
                        connection.expire(finalKey.getBytes(), timeout);
                        return connection.get(finalKey.getBytes());
                    }
                });
            }
            return obj != null?new SimpleValueWrapper(obj):null;
        }
	}

	@Override
	public void evict(Object key) {
		if(key == null) {
			return;
		}else {
			final String finalKey = formatStr(key);
			redisTemplate.multi();
			redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					connection.del(finalKey.getBytes());
					return true;
				}
			});
			redisTemplate.exec();
		}
	}

	@Override
	public void clear() {
		redisTemplate.multi();
		redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return true;
			}
		});
		redisTemplate.exec();
	}
	
	private String formatStr(Object obj) {
		if(obj instanceof String) {
			return (String)obj;
		}else {
			return obj.toString();
		}
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setName(String name) {
		this.name = name;
	}

}
