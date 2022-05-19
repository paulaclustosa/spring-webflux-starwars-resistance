package com.letscode.starwars.repository;

import com.letscode.starwars.domain.Rebel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RebelRepository extends ReactiveMongoRepository<Rebel, String> {

  Mono<Rebel> findByPublicId(UUID publicId);

}
