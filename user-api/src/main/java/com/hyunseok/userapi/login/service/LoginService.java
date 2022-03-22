package com.hyunseok.userapi.login.service;

import com.hyunseok.userapi.vo.BasicUser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private Map<String, String> userMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public static String ERROR = "error!";
    public static String FAIL = "loginFail!";

    public Mono<String> join(BasicUser user) {
        String name = user.getName();
        String email = user.getEmail();
        boolean nameContains = userMap.containsKey(name);
        if (nameContains) {
            return Mono.just("join false! name exist");
        } else {
            userMap.put(name, email);
            return Mono.just(String.format("success! welcome %s", name));
        }
    }

    public Mono<String> login(BasicUser user) {
        String result;
        if (userMap.containsKey(user.getName()) &&
                userMap.get(user.getName()).equals(user.getEmail())) {
            String userJson;
            try {
                 result = objectMapper.writeValueAsString(user);
            } catch (Exception e) {
                e.printStackTrace();
                result = ERROR;
            }
        } else {
            result = FAIL;
        }
        return Mono.just(result);
    }
}
