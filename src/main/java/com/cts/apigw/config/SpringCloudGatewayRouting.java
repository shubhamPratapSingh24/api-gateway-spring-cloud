package com.cts.apigw.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudGatewayRouting {
    @Bean
    public RouteLocator configureRoute(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route( p -> p.path("/todolist/api/v1/user/list/all").uri("http://localhost:8082"))
                .build();
        /*return builder.routes()
                .route("paymentId", r->r.path("/payment/**").uri("http://localhost:9009")) //static routing
                .route("orderId", r->r.path("/order/**").uri("lb://ORDER-SERVICE")) //dynamic routing
                .build();*/
    }
}
