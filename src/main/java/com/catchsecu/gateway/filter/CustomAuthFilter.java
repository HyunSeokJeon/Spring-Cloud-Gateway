package com.catchsecu.gateway.filter;

import com.catchsecu.gateway.jwt.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {

    public CustomAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        // exchange : ServerWebExchange
        // chain : GatewayFilterChain
        return ((exchange, chain) -> {
            // ServerHttpRequest httpRequest = exchange.getRequest(); : Pre Filter
            // ServerHttpResponse response = exchange.getResponse(); : Post Filter
            // not ServletRequest, is SpringReactive Req, Res
            ServerHttpRequest httpRequest = exchange.getRequest();

            if (!httpRequest.getHeaders().containsKey("token")) {
                return handleUnAuthorized(exchange);
            } else {
                List<String> token = httpRequest.getHeaders().get("token");
                String tokenString = Objects.requireNonNull(token).get(0);
                Map<String, String> payloadMap = new JwtUtil().payloadByValidToken(tokenString);
                URI uri = exchange.getRequest().getURI();
                StringBuilder query = new StringBuilder();
                String originalQuery = uri.getRawQuery();

                if (StringUtils.hasText(originalQuery)) {
                    query.append(originalQuery);
                    if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                        query.append('&');
                    }
                }
                for (Map.Entry<String, String> payload : payloadMap.entrySet()) {
                    String value = ServerWebExchangeUtils.expand(exchange, payload.getValue());
                    // TODO urlencode?
                    query.append(payload.getKey());
                    query.append('=');
                    query.append(value);
                    query.append('&');
                }
                query.delete(query.lastIndexOf("&"), query.lastIndexOf("&") + 1);

                try {
                    URI newUri = UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build(true).toUri();

                    ServerHttpRequest request = exchange.getRequest().mutate().uri(newUri).build();

                    return chain.filter(exchange.mutate().request(request).build());
                } catch (RuntimeException ex) {
                    throw new IllegalStateException("Invalid URI query: \"" + query.toString() + "\"");
                }
            }



        });
    }

    // curl --verbose http://localhost:8090/user/info
    // 401
    // curl -v -H "token: hyunToken" http://localhost:8090/user/info
    // 200

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
    }

}
