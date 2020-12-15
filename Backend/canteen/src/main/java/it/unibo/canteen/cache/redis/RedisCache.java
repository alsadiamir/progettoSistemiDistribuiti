package it.unibo.canteen.cache.redis;

import com.google.gson.Gson;
import it.unibo.canteen.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisCache<T> implements Cache<T> {
    private final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);
    private final String baseKey;
    private final Class<T> ofClass;

    @Value("${redis.cache.keyTimeToLiveInMinutes}")
    private long redisTimeToLiveInMinutes;

    @Autowired
    private RedisTemplate<String, List<String>> redisTemplate;

    private HashOperations<String, String, List<String>> hashOperations;

    protected HashOperations<String, String, List<String>> getHashOperations() {
        if (hashOperations == null) {
            hashOperations = redisTemplate.opsForHash();
        }
        return hashOperations;
    }

    public RedisCache(String baseKey, Class<T> ofClass) {
        this.baseKey = baseKey;
        this.ofClass = ofClass;
    }

    @Override
    public Optional<List<T>> get(String id) throws IOException {
        try {
            Optional<List<String>> list = Optional.ofNullable(getHashOperations().get(baseKey, id));
            if(list.isEmpty()) {
                LOGGER.info("REDIS: Cache miss for id " + id);
                return Optional.empty();
            }
            LOGGER.info("REDIS: Cache hit for id " + id);
            return Optional.of(list.get().stream().map(s -> new Gson().fromJson(s, ofClass)).collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.error("REDIS: Failed GET operation for id " + id);
            throw new IOException(e);
        }
    }

    @Override
    public Optional<T> getAny(String id) throws IOException {
        Optional<List<T>> list = get(id);
        if(list.isEmpty()) {
            return Optional.empty();
        }
        return list.get().stream().findAny();
    }

    @Override
    public void put(String id, List<T> value) throws IOException {
        try {
            getHashOperations().put(baseKey, id, value.stream().map(v -> new Gson().toJson(v)).collect(Collectors.toList()));
            redisTemplate.expire(id, redisTimeToLiveInMinutes, TimeUnit.MINUTES);
            LOGGER.info("REDIS: Put in cache for id " + id);
        } catch (Exception e) {
            LOGGER.error("REDIS: Failed PUT operation for id " + id);
            throw new IOException(e);
        }
    }

    @Override
    public void put(String id, T value) throws IOException {
        put(id, List.of(value));
    }

    @Override
    public boolean delete(String id) throws IOException {
        try {
            boolean res = getHashOperations().delete(baseKey, id) > 0;
            if (res) {
                LOGGER.info("REDIS: Delete in cache for id " + id);
            } else {
                LOGGER.info("REDIS: Delete no object in cache for id " + id);
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("REDIS: Failed DELETE operation for id " + id);
            throw new IOException(e);
        }
    }
}
