package com.catchsecu.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class LoginFilter extends AbstractGatewayFilterFactory<LoginFilter.Config> {

    public LoginFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            RequestPath path = request.getPath();

            if (path.value().contains("/auth")) {
                ServerHttpResponse response = exchange.getResponse();
                HttpStatus statusCode = response.getStatusCode();
            }

            return chain.filter(exchange).then();
        });
    }

    public static class Config {
    }

}
