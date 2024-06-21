package org.ncba.main.repository;

import org.ncba.main.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @ Author Nephat Muchiri
 * Date 20/06/2024
 */
public interface UserRepository extends ReactiveCrudRepository<User, Long > {
    Mono<User> findByMsisdn(String msisdn);
    Mono<User> findByDocumentNumber(String documentNumber);
    Flux<User> findByDeletedFalse();
    Flux<User> findByDeletedTrue();
    Mono<User> findByIdAndDeletedFalse(Long id);
}
