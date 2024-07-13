package cn.lokn.kngateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

/**
 * gateway plugin.
 */
public interface GatewayPlugin {

    String GATEWAY_PREFIX = "/gw";

    void start();
    void stop();

    String getName();

    boolean support(ServerWebExchange exchange);

    Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain);

}
