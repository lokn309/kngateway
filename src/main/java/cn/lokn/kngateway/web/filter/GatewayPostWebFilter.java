package cn.lokn.kngateway.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @description:
 * @author: lokn
 * @date: 2024/06/27 00:21
 */
@Component
public class GatewayPostWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 该类执行很晚，很多事无法处理
        return chain.filter(exchange).doFinally(
                s -> {
                    System.out.println(" ===>>> post filter");
                    exchange.getAttributes().forEach((k, v) -> System.out.println(k + ":" + v));
                }
        );
    }
}
