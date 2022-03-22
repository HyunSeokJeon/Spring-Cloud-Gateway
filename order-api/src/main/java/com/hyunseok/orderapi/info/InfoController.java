package com.hyunseok.orderapi.info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @GetMapping("/info")
    public String info(@Value("${server.port}") String port,
                       @RequestParam("name") String name,
                       @RequestParam("email") String email) {
        return String.format("Order 서비스 기본 동작 Port : %s, " +
                "파라미터 name : %s, " +
                "파라미터 email : %s",
                port, name, email);
    }
}
