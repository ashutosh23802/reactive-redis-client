package tiaa.in.reactiveredisclient.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RedisServiceTest {

    @InjectMocks
    private RedisService redisService;

    @Mock
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Mock
    private ReactiveValueOperations reactiveValueOperations;
    @Mock
    private ReactiveListOperations reactiveListOperations;
    @Mock
    private ReactiveHashOperations reactiveHashOperations;
    @Mock
    private DataType dataType;

    @Test
    public void shouldGetAllKeys() {
        when(reactiveStringRedisTemplate.keys(any())).thenReturn(Flux.just("key1", "key2", "key3"));
        Flux<String> keys = redisService.getAllKeys();
        StepVerifier
                .create(keys)
                .consumeNextWith(key -> {
                    assertEquals(key, "key1");
                })
                .consumeNextWith(key -> {
                    assertEquals(key, "key2");
                })
                .consumeNextWith(key -> {
                    assertEquals(key, "key3");
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetValue() {
        when(reactiveStringRedisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
        when(reactiveValueOperations.get(any())).thenReturn(Mono.just("value"));
        Mono<String> values = redisService.getValue("key");
        StepVerifier
                .create(values)
                .consumeNextWith(value -> {
                    assertEquals(value, "value");
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetDataTypeOfValueForKey() {
        when(reactiveStringRedisTemplate.type(any())).thenReturn(Mono.just(dataType));
        when(dataType.name()).thenReturn("STRING");
        Mono<String> values = redisService.getDataTypeOfValue("key");
        StepVerifier
                .create(values)
                .consumeNextWith(value -> {
                    assertEquals(value, "STRING");
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetAllElementsInList() {
        when(reactiveStringRedisTemplate.opsForList()).thenReturn(reactiveListOperations);
        when(reactiveListOperations.range("listName", 0, -1)).thenReturn(Flux.just("value1", "value2", "value3"));
        Flux<String> listElements = redisService.getValuesInList("listName");
        StepVerifier
                .create(listElements)
                .consumeNextWith(element -> {
                    assertEquals(element, "value1");
                })
                .consumeNextWith(element -> {
                    assertEquals(element, "value2");
                })
                .consumeNextWith(element -> {
                    assertEquals(element, "value3");
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetAllHashEntries() {
        Map<Object, Object> mockData = Map.of("key1", "value1", "key2", "value2");
        when(reactiveStringRedisTemplate.opsForHash()).thenReturn(reactiveHashOperations);
        when(reactiveHashOperations.entries(any())).thenReturn(Flux.fromStream(mockData.entrySet().stream()));
        Flux<Map.Entry<Object, Object>> hashEntries = redisService.getHashEntries("hasKey");
        StepVerifier
                .create(hashEntries)
                .expectNextCount(2)
                .verifyComplete();
    }

}