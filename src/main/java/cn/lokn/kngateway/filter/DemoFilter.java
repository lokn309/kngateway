package cn.lokn.kngateway.filter;

import cn.lokn.kngateway.GatewayFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @description:
 * @author: lokn
 * @date: 2024/07/13 10:55
 */
@Component("demoFilter")
public class DemoFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        System.out.println(" ===>>> filters: demo filter ...");
        exchange.getRequest().getHeaders().toSingleValueMap()
                .forEach((k, v) -> System.out.println(k + " : " + v));
        return Mono.empty();
    }
}
