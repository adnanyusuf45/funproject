package com.funproject.funproject.router;

import com.funproject.funproject.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterConfig {

    @Autowired
    private UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route()
                .POST("api/registeruser",userHandler::registerUser)
                .POST("api/loginuser",userHandler::loginUser)
                .build();
    }
}
