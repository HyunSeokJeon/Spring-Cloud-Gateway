package com.catchsecu.gateway;

import com.catchsecu.gateway.filter.CustomAuthFilter;
import com.catchsecu.gateway.jwt.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class Router {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-api", predicateSpec ->
                        predicateSpec.path("/user/**")
                                .filters(gatewayFilterSpec ->
                                        gatewayFilterSpec.rewritePath("/user/?(?<segment>.*)", "/${segment}")
                                )
                                .uri("lb://USER-API/")
                )
                .route("user-auth", predicateSpec ->
                        predicateSpec.path("/userLogin/**")
                                .filters(gatewayFilterSpec ->
                                        gatewayFilterSpec.rewritePath("/userLogin/?(?<segment>.*)", "/${segment}")
                                                .modifyResponseBody(String.class, String.class, ((serverWebExchange, s) -> Mono.just(new JwtUtil().createToken(s))))
                                )
                                .uri("lb://USER-API/")
                )
                .route("order-api", predicateSpec ->
                        predicateSpec.path("/order/**")
                                .filters(gatewayFilterSpec ->
                                        gatewayFilterSpec.rewritePath("/order/?(?<segment>.*)", "/${segment}")
                                                .filter(new CustomAuthFilter().apply(new CustomAuthFilter.Config())))
                                .uri("lb://ORDER-API/")
                )
                .build();
    }}
