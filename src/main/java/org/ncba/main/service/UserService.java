package org.ncba.main.service;

import org.ncba.main.entity.User;
import org.ncba.main.model.WsResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @ Author Nephat Muchiri
 * Date 20/06/2024
 */
public interface UserService {
    Flux<WsResponse<User>> getAllUsers(Map<String, String> headers);
    Flux<WsResponse<User>> getAllDeletedUsers(Map<String, String> headers);
    Mono<WsResponse<User>> getUserById(Long id, Map<String, String> headers);
    Mono<WsResponse<User>> getUserByMSISDN(String msisdn, Map<String, String> headers);
    Mono<WsResponse<User>> getUserByDocumentNumber(String documentNumber, Map<String, String> headers);
    Mono<WsResponse<User>> createUser(User user, Map<String, String> headers);
    Mono<WsResponse<User>> updateUser(Long id, User user, Map<String, String> headers);
    Mono<WsResponse<Void>> deleteUser(Long id, Map<String, String> headers);
    Mono<WsResponse<User>> restoreUser(Long id, Map<String, String> headers);
}
