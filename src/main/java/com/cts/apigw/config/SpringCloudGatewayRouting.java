package com.cts.apigw.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableDiscoveryClient
public class SpringCloudGatewayRouting {

    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(loadBalancerClientFactory
                .getLazyProvider(name, ServiceInstanceListSupplier.class),
                name);
    }

    @Bean
    DiscoveryClientRouteDefinitionLocator discoveryRoutes(ReactiveDiscoveryClient rdc,
                                                          DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        // adding 2 rotes to first microservice as we need to log request body if method is POST
        return builder.routes()
                .route("TODO-SERVICE",r -> r.path("/todolist/**")
                        .and().method("GET")
                        .uri("lb://TODO-SERVICE"))
               /* .route("first-microservice",r -> r.path("/first")
                        .and().method("GET").filters(f-> f.filters(authFilter))
                        .uri("http://localhost:8081"))
                .route("second-microservice",r -> r.path("/second")
                        .and().method("POST")
                        .and().readBody(Company.class, s -> true).filters(f -> f.filters(requestFilter, authFilter))
                        .uri("http://localhost:8082"))
                .route("second-microservice",r -> r.path("/second")
                        .and().method("GET").filters(f-> f.filters(authFilter))
                        .uri("http://localhost:8082"))
                .route("auth-server",r -> r.path("/login")
                        .uri("http://localhost:8088"))*/
                .build();
    }

//    @Bean
//    public RouteLocator configureRoute(RouteLocatorBuilder routeLocatorBuilder) {
//        return routeLocatorBuilder.routes()
//                .route( p -> p.path("/todolist/api/v1/user/list/all").uri("http://localhost:8082"))
//                .build();
//        /*return builder.routes()
//                .route("paymentId", r->r.path("/payment/**").uri("http://localhost:9009")) //static routing
//                .route("orderId", r->r.path("/order/**").uri("lb://ORDER-SERVICE")) //dynamic routing
//                .build();*/
//    }
}
