package com.hyunseok.orderapi.info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @GetMapping("/order/info")
    public String info(@Value("${server.port}") String port) {
        return "Order 서비스 기본 동작 Port : " + port;
    }
}
