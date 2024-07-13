package cn.lokn.kngateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

/**
 * @description:
 * @author: lokn
 * @date: 2024/07/07 21:42
 */
public abstract class AbstractGatewayPlugin implements GatewayPlugin {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain) {
        boolean support = support(exchange);
        System.out.println("===>>> plugin[" + this.getName() + "], support=" + support);
        return support ? doHandle(exchange, chain) : chain.handle(exchange);
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    public abstract Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain);

    public abstract boolean doSupport(ServerWebExchange exchange);

}
