package org.ncba.main.controller;

import jakarta.validation.Valid;
import org.ncba.main.entity.User;
import org.ncba.main.model.UserSearchRequest;
import org.ncba.main.model.WsHeader;
import org.ncba.main.model.WsResponse;
import org.ncba.main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @ Author Nephat Muchiri
 * Date 20/06/2024
 */
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserServiceController {

    private final UserService userService;

    public UserServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<WsResponse<User>> getAllUsers(@RequestHeader Map<String, String> headers) {
        return userService.getAllUsers(headers);
    }

    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    public Flux<WsResponse<User>> getDeletedUsers(@RequestHeader Map<String, String> headers) {
        return userService.getAllDeletedUsers(headers);
    }

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WsResponse<User>> searchUser(@Valid @RequestBody UserSearchRequest request, @RequestHeader Map<String, String> headers) {
        if (request.getUserId() != null) {
            return userService.getUserById(request.getUserId(), headers);
        } else if (request.getMsisdn() != null) {
            return userService.getUserByMSISDN(request.getMsisdn(), headers);
        } else if (request.getDocumentNumber() != null) {
            return userService.getUserByDocumentNumber(request.getDocumentNumber(), headers);
        } else {
            return Mono.just(new WsResponse<>(new WsHeader(headers.get("requestRefId"), 400, "Bad Request", "No search criteria provided", null), null));
        }
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WsResponse<User>> createUser(@Valid @RequestBody User user, @RequestHeader Map<String, String> headers) {
        return userService.createUser(user, headers);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WsResponse<User>> updateUser(@RequestParam Long id, @Valid @RequestBody User user, @RequestHeader Map<String, String> headers) {
        return userService.updateUser(id, user, headers);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WsResponse<Void>> deleteUser(@RequestParam Long id, @RequestHeader Map<String, String> headers) {
        return userService.deleteUser(id, headers);
    }

    @PutMapping("/restore")
    @ResponseStatus(HttpStatus.OK)
    public Mono<WsResponse<User>> restoreUser(@RequestParam Long id, @RequestHeader Map<String, String> headers) {
        return userService.restoreUser(id, headers);
    }
}
