package com.letscode.starwars.controller.handler;

import com.letscode.starwars.dto.*;
import com.letscode.starwars.service.RebelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RebelHandler {

  final RebelService service;

  @Autowired
  public RebelHandler(RebelService service) {
    this.service = service;
  }

  public Mono<ServerResponse> createRebel(ServerRequest request) {
    Mono<CreateRebelRequest> rebelMono = request.bodyToMono(CreateRebelRequest.class);

    return ServerResponse.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromPublisher(rebelMono.flatMap(service::create), RebelResponse.class));
  }

  public Mono<ServerResponse> getRebel(ServerRequest request) {
    String id = request.pathVariable("id");
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.getByPublicId(id), RebelResponse.class);
  }

  public Mono<ServerResponse> getAll(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.getAll(), RebelResponse.class);
  }

  public Mono<ServerResponse> reportAsTraitor(ServerRequest request) {
    String id = request.pathVariable("id");
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.reportAsTraitor(id), ReportAsTraitorResponse.class);
  }

  public Mono<ServerResponse> deleteAll(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.deleteAll(), DeleteRebelsResponse.class);
  }

  public Mono<ServerResponse> updateLocation(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<UpdateLocationRequest> locationMono = request.bodyToMono(UpdateLocationRequest.class);

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromPublisher(locationMono.flatMap(updateLocationRequest -> service.updateLocation(id, updateLocationRequest)), RebelResponse.class));
  }

}