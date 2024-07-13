package cn.lokn.kngateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway plugin chain.
 */
public interface GatewayPluginChain {

    Mono<Void> handle(ServerWebExchange exchange);

}
