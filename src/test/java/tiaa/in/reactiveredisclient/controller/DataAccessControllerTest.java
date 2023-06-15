package tiaa.in.reactiveredisclient.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tiaa.in.reactiveredisclient.service.RedisService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(DataAccessController.class)
public class DataAccessControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private RedisService redisService;

    @Test
    public void shouldGetAllKeys() {
        when(redisService.getAllKeys()).thenReturn(Flux.just("key1", "key2", "key3"));

        webClient
                .get().uri("/keys")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class);
    }

    @Test
    public void shouldGetValueForKey() {
        when(redisService.getValue("key")).thenReturn(Mono.just("value"));

        webClient
                .get().uri("/value/key")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class);
    }

    @Test
    public void shouldGetDataTypeOfValueStoredForKey() {
        when(redisService.getDataTypeOfValue("key")).thenReturn(Mono.just("String"));

        webClient
                .get().uri("/dataType/key")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class);
    }

    @Test
    public void shouldGetListValues() {
        when(redisService.getValuesInList("listName")).thenReturn(Flux.just("value1", "value2", "value3"));

        webClient
                .get().uri("/list/listName")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class);

    }

    @Test
    public void shouldHashEntries() {
        Map<Object,Object> mockData = Map.of("key1","value1","key2","value2");
        when(redisService.getHashEntries("hashKey")).thenReturn(Flux.fromStream(mockData.entrySet().stream()));

        webClient
                .get().uri("/hash/hashKey")
                .exchange()
                .expectStatus()
                .isOk();

    }

}
