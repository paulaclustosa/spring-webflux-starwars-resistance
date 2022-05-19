package com.letscode.starwars.service;

import com.letscode.starwars.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRebelService {

  Mono<RebelResponse> create(CreateRebelRequest request);
  Mono<RebelResponse> getByPublicId(String rebelId);
  Flux<RebelResponse> getAll();
  Mono<RebelResponse> updateLocation(String rebelId, UpdateLocationRequest request);
  Mono<DeleteRebelsResponse> deleteAll();
  Mono<ReportAsTraitorResponse> reportAsTraitor(String rebelId);

}
