package com.letscode.starwars.controller.handler;

import com.letscode.starwars.dto.*;
import com.letscode.starwars.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TradeHandler {

  final TradeService service;

  @Autowired
  public TradeHandler(TradeService service) {
    this.service = service;
  }

  public Mono<ServerResponse> handleTrade(ServerRequest request) {
    Flux<TradeInventoryItemsRequest> tradeItemMono = request.bodyToFlux(TradeInventoryItemsRequest.class);

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromPublisher(tradeItemMono.flatMap(service::handleTrade), RebelResponse.class));
  }

}
