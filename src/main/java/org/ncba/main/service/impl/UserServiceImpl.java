package org.ncba.main.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.ncba.main.entity.User;
import org.ncba.main.model.WsHeader;
import org.ncba.main.model.WsResponse;
import org.ncba.main.repository.UserRepository;
import org.ncba.main.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.r2dbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

import static org.ncba.main.util.UtilService.normalizeMSISDN;

/**
 * @ Author Nephat Muchiri
 * Date 20/06/2024
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Flux<WsResponse<User>> getAllUsers(Map<String, String> headers) {
        // Retrieves all users who are not marked as deleted
        return userRepository.findByDeletedFalse()
                .map(user -> createWsResponse(user, "Success", 200, headers.get("requestRefId")));
    }

    @Override
    public Flux<WsResponse<User>> getAllDeletedUsers(Map<String, String> headers) {
        // Retrieves all users who are marked as deleted
        return userRepository.findByDeletedTrue()
                .map(user -> createWsResponse(user, "Success", 200, headers.get("requestRefId")));
    }

    @Override
    public Mono<WsResponse<User>> getUserById(Long id, Map<String, String> headers) {
        // Retrieves a user by ID, if not found returns a 404 response
        return userRepository.findById(id)
                .map(user -> createWsResponse(user, "Success", 200, headers.get("requestRefId")))
                .defaultIfEmpty(createWsResponse(null, "User not found", 404, headers.get("requestRefId")));
    }

    @Override
    public Mono<WsResponse<User>> getUserByMSISDN(String msisdn, Map<String, String> headers) {
        // Normalizes MSISDN and retrieves a user by MSISDN, if not found returns a 404 response
        msisdn = normalizeMSISDN(msisdn);
        return userRepository.findByMsisdn(msisdn)
                .map(user -> createWsResponse(user, "Success", 200, headers.get("requestRefId")))
                .defaultIfEmpty(createWsResponse(null, "User not found", 404, headers.get("requestRefId")));
    }

    @Override
    public Mono<WsResponse<User>> getUserByDocumentNumber(String documentNumber, Map<String, String> headers) {
        // Retrieves a user by document number, if not found returns a 404 response
        return userRepository.findByDocumentNumber(documentNumber)
                .map(user -> createWsResponse(user, "Success", 200, headers.get("requestRefId")))
                .defaultIfEmpty(createWsResponse(null, "User not found", 404, headers.get("requestRefId")));
    }

    @Override
    public Mono<WsResponse<User>> createUser(User user, Map<String, String> headers) {
        // Normalizes MSISDN, attempts to save a new user, and handles potential errors

        String msisdn = user.getMsisdn();
        msisdn = normalizeMSISDN(msisdn);
        user.setMsisdn(msisdn);
        return userRepository.save(user)
                .map(savedUser -> createWsResponse(savedUser, "User created successfully", 201, headers.get("requestRefId")))
                .onErrorResume(DataIntegrityViolationException.class, e -> {
                    String errorMessage = "User creation failed: Duplicate entry for a unique field.";
                    return Mono.just(createWsResponse(null, errorMessage, 400, headers.get("requestRefId")));
                })
                .onErrorResume(BadSqlGrammarException.class, e -> {
                    String errorMessage = "User creation failed: Invalid SQL syntax.";
                    return Mono.just(createWsResponse(null, errorMessage, 400, headers.get("requestRefId")));
                })
                .onErrorResume(Exception.class, e -> {
                    String errorMessage = "User creation failed: " + e.getMessage();
                    return Mono.just(createWsResponse(null, errorMessage, 500, headers.get("requestRefId")));
                });
    }

    @Override
    public Mono<WsResponse<User>> updateUser(Long id, User user, Map<String, String> headers) {
        // Normalizes MSISDN, updates an existing user's details, and handles potential errors

        String msisdn = user.getMsisdn();
        msisdn = normalizeMSISDN(msisdn);
        user.setMsisdn(msisdn);

        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    // Update other fields as necessary
                    return userRepository.save(existingUser);
                })
                .map(updatedUser -> createWsResponse(updatedUser, "User updated successfully", 200, headers.get("requestRefId")))
                .defaultIfEmpty(createWsResponse(null, "User not found", 404, headers.get("requestRefId")))
                .onErrorResume(e -> Mono.just(createWsResponse(null, "User update failed: " + e.getMessage(), 500, headers.get("requestRefId"))));
    }

    /**
     * Marks a user as deleted.
     *
     * @param id The ID of the user to be deleted.
     * @param headers The HTTP headers containing additional request information.
     * @return A Mono of WsResponse indicating whether the deletion was successful or not.
     */
    @Override
    public Mono<WsResponse<Void>> deleteUser(Long id, Map<String, String> headers) {
        return userRepository.findByIdAndDeletedFalse(id)
                .flatMap(existingUser -> {
                    existingUser.setDeleted(true);
                    return userRepository.save(existingUser)
                            .then(Mono.just(createWsResponse("User deleted successfully", 200)));
                })
                .defaultIfEmpty(createWsResponse("User not found", 404))
                .onErrorResume(e -> Mono.just(createWsResponse("User deletion failed: " + e.getMessage(), 500)));
    }

    @Override
    public Mono<WsResponse<User>> restoreUser(Long id, Map<String, String> headers) {
        // Restores a deleted user by setting the 'deleted' flag to false

        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setDeleted(false);
                    return userRepository.save(existingUser)
                            .then(Mono.just(createWsResponse(existingUser, "User restored successfully", 200, headers.get("requestRefId"))));
                })
                .defaultIfEmpty(createWsResponse(null, "User not found", 404, headers.get("requestRefId")))
                .onErrorResume(e -> Mono.just(createWsResponse(null, "User restoration failed: " + e.getMessage(), 500, headers.get("requestRefId"))));
    }

    private <T> WsResponse<T> createWsResponse(T body, String message, int code, String requestRefId) {
        WsHeader header = WsHeader.builder()
                .requestRefId(requestRefId)
                .responseCode(code)
                .responseMessage(message)
                .customerMessage(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
        return new WsResponse<>(header, body);
    }

    private WsResponse<Void> createWsResponse(String message, int code) {
        WsHeader header = WsHeader.builder()
                .responseCode(code)
                .responseMessage(message)
                .customerMessage(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
        return new WsResponse<>(header, null);
    }
}
