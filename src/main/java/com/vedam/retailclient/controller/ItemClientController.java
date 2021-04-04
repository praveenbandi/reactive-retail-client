package com.vedam.retailclient.controller;

import com.vedam.retailclient.domain.Item;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.vedam.retailclient.constants.URLConstants.*;

@RestController
public class ItemClientController {
    WebClient webClient = WebClient.create(BASE_URL);

    @GetMapping(CLIENT_RETRIEVE)
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri(ITEM_ENDPOINT_V1)
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items retrieved: ");
    }

    @GetMapping(CLIENT_EXCHANGE)
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient.get().uri(ITEM_ENDPOINT_V1)
                .exchangeToFlux(response -> response.bodyToFlux(Item.class))
                .log("Items exchanged: ");
    }

    @GetMapping(CLIENT_EXCHANGE_ONE_ITEM)
    public Mono<Item> getItemUsingExchange() {
        String id = "XYZ123";
        return webClient.get().uri(ITEM_ENDPOINT_V1 + "/{id}", id)
                .exchangeToMono(response -> response.bodyToMono(Item.class))
                .log("Items exchanged: ");
    }

    @PostMapping(CLIENT_ITEM)
    public Mono<Item> createItem(@RequestBody Item item) {
        return webClient.post().uri(ITEM_ENDPOINT_V1)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log();
    }

    @PutMapping(CLIENT_ITEM+"/{id}")
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        return webClient.put().uri(ITEM_ENDPOINT_V1+"/{id}", id)
                .body(Mono.just(item), Item.class)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Item.class))
                .log();

    }

    @DeleteMapping(CLIENT_ITEM+"/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return webClient.delete().uri(ITEM_ENDPOINT_V1+"/{id}", id)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Void.class))
                .log();
    }
}
