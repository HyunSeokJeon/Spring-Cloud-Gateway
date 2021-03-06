package com.hyunseok.userapi.info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @GetMapping("/info")
    public String info(@Value("${server.port}") String port) {
        return "User 서비스 기본 동작 Port : " + port;
    }
}
