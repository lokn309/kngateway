package cn.lokn.kngateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @description:
 * @author: lokn
 * @date: 2024/07/13 10:52
 */
public interface GatewayFilter {

    Mono<Void> filter(ServerWebExchange exchange);

}
