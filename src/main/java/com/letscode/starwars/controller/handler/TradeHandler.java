package com.letscode.starwars.controller.handler;

import com.letscode.starwars.dto.*;
import com.letscode.starwars.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class TradeHandler {

  final TradeService service;

  @Autowired
  public TradeHandler(TradeService service) {
    this.service = service;
  }

  public Mono<ServerResponse> handleTrade(ServerRequest request) {
    Optional<String> rebelFirstId = request.queryParam("rebelFirstId");
    Optional<String> rebelSecondId  = request.queryParam("rebelSecondId");
    Flux<TradeInventoryItemsRequest> itemsToBeTradeFlux = request.bodyToFlux(TradeInventoryItemsRequest.class);

    if (rebelFirstId.isPresent() && rebelSecondId.isPresent()) {
      return ServerResponse.ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromPublisher(itemsToBeTradeFlux.flatMap(itemsToBeTrade ->
              service.handleTrade(rebelFirstId.get(), rebelSecondId.get(), itemsToBeTrade)), RebelResponse.class));
    }
    else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It is mandatory to informed rebels id involved in trade (as query params).");
  }

}
