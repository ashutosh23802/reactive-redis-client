package tiaa.in.reactiveredisclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class RedisService {

    @Autowired
    ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public Flux<String> getAllKeys() {
        return reactiveStringRedisTemplate.keys("*");
    }

    public Mono<String> getValue(String key) {
        return reactiveStringRedisTemplate.opsForValue().get(key);
    }

    public Mono<String> getDataTypeOfValue(String key) {
        return reactiveStringRedisTemplate.type(key).map(dataType -> dataType.name());
    }
    public Flux<String> getValuesInList(String listName) {
        return  reactiveStringRedisTemplate.opsForList().range("listName",0,-1);
    }

    public Flux<Map.Entry<Object, Object>> getHashEntries(String hashKey) {
        return  reactiveStringRedisTemplate.opsForHash().entries(hashKey);
    }
}
