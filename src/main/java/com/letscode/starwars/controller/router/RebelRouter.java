package com.letscode.starwars.controller.router;

import com.letscode.starwars.controller.handler.RebelHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RebelRouter {

  @Bean
  public RouterFunction<ServerResponse> routeRebel(RebelHandler handler) {
    return route()
        .path("/v1/rebels", b1 -> b1
            .nest(accept(APPLICATION_JSON), b2 -> b2
                .POST("", handler::createRebel)
                .GET("/{id}", handler::getRebel)
                .GET("", handler::getAll)
                .DELETE("", handler::deleteAll)
                .PATCH("/{id}", handler::reportAsTraitor)
                .PATCH("", handler::updateLocation)))
            .build();
  }

}
