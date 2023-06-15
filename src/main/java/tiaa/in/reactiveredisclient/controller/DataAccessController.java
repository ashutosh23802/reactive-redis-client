package tiaa.in.reactiveredisclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tiaa.in.reactiveredisclient.service.RedisService;

import java.util.Map;

@RestController
public class DataAccessController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/keys")
    ResponseEntity<Flux<String>> getAllKeys() {
        return new ResponseEntity<>(redisService.getAllKeys(), HttpStatus.OK);
    }

    @GetMapping("/value/{key}")
    ResponseEntity<Mono<String>> getValue(@PathVariable String key) {
        return new ResponseEntity<>(redisService.getValue(key), HttpStatus.OK);
    }

    @GetMapping("/dataType/{key}")
    ResponseEntity<Mono<String>> getDataTypeOfValueForKey(@PathVariable String key) {
        return new ResponseEntity<>(redisService.getValue(key), HttpStatus.OK);
    }

    @GetMapping("/list/{key}")
    ResponseEntity<Flux<String>> getAllListElements(@PathVariable String key) {
        return new ResponseEntity<>(redisService.getValuesInList(key), HttpStatus.OK);
    }

    @GetMapping("/hash/{key}")
    ResponseEntity<Flux<Map.Entry<Object, Object>>> getAllHashEntries(@PathVariable String key) {
        return new ResponseEntity<>(redisService.getHashEntries(key), HttpStatus.OK);
    }
}
