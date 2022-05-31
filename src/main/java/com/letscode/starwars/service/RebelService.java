package com.letscode.starwars.service;

import com.letscode.starwars.domain.*;
import com.letscode.starwars.dto.*;
import com.letscode.starwars.repository.RebelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class RebelService implements IRebelService {

  private final RebelRepository repository;

  @Autowired
  public RebelService(RebelRepository repository) {
    this.repository = repository;
  }

  @Override
    public Mono<RebelResponse> create(CreateRebelRequest request) {
    Rebel rebel = RebelMapper.toRebel(request);
    return repository.save(rebel).map(RebelMapper::toRebelResponse);
  }

  @Override
  public Mono<RebelResponse> getByPublicId(String rebelId) {
    UUID uuidId = UUID.fromString(rebelId);
    return repository.findByPublicId(uuidId).map(RebelMapper::toRebelResponse);
  }

  @Override
  public Flux<RebelResponse> getAll() {
    return repository.findAll().map(RebelMapper::toRebelResponse);
  }

  @Override
  public Mono<RebelResponse> updateLocation(String rebelId, UpdateLocationRequest request) {
    UUID uuidId = UUID.fromString(rebelId);
    Mono<Rebel> rebelMono = repository.findByPublicId(uuidId)
        .doOnSuccess(rebelFromDb -> {
          Location newLocation = Location.builder()
              .galaxy(request.getGalaxy())
              .latitude(request.getLatitude())
              .longitude(request.getLongitude())
              .build();
          rebelFromDb.setLocation(newLocation);
          repository.save(rebelFromDb);
        });

    return rebelMono.flatMap(rebel -> repository.save(rebel).map(RebelMapper::toRebelResponse));
  }

  @Override
  public Mono<DeleteRebelsResponse> deleteAll() {
    return repository.deleteAll().thenReturn(new DeleteRebelsResponse(true));
  }

  @Override
  public Mono<ReportAsTraitorResponse> reportAsTraitor(String id) {
    UUID uuidId = UUID.fromString(id);

    Mono<Rebel> rebelMono = repository.findByPublicId(uuidId)
        .doOnSuccess(rebelFromDb -> {
          rebelFromDb.updateNumberReportAsTraitor();
          rebelFromDb.updateIsTraitor();
        });

    return rebelMono.flatMap(rebel -> repository.save(rebel).map(RebelMapper::toReportAsTraitorResponse));
  }

}
