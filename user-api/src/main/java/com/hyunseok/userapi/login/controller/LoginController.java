package com.hyunseok.userapi.login.controller;

import com.hyunseok.userapi.login.service.LoginService;
import com.hyunseok.userapi.vo.BasicUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/join")
    public Mono<ResponseEntity<String>> memberJoin(BasicUser user) {
        Mono<String> result = loginService.join(user);
        return result.map(msg -> {
            if (msg.contains("false")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        });
    }

    @PostMapping("/auth/login")
    public Mono<ResponseEntity<String>> memberLogin(BasicUser user) {
        Mono<String> result = loginService.login(user);
        return result.map(msg -> {
            if (LoginService.ERROR.equals(msg) || LoginService.FAIL.equals(msg)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                return ResponseEntity.ok().body(msg);
            }
        });
    }
}
