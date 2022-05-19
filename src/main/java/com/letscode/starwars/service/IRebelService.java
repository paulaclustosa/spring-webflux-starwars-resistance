package com.letscode.starwars.service;

import com.letscode.starwars.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRebelService {

  Mono<RebelResponse> create(CreateRebelRequest request);
  Mono<RebelResponse> getByPublicId(String id);
  Flux<RebelResponse> getAll();
  Mono<ReportAsTraitorResponse> reportAsTraitor(String id);
  Mono<DeleteRebelsResponse> deleteAll();
  Mono<RebelResponse> updateLocation(UpdateLocationRequest request);

}
