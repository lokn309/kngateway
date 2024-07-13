package cn.lokn.kngateway.plugin;

import cn.kimmking.kkrpc.core.api.LoadBalancer;
import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.cluster.RoundRibonLoadBalancer;
import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import cn.kimmking.kkrpc.core.meta.ServiceMeta;
import cn.lokn.kngateway.AbstractGatewayPlugin;
import cn.lokn.kngateway.GatewayPluginChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @description:
 * @author: lokn
 * @date: 2024/07/07 21:51
 */
@Component("knrpc")
public class KNRpcPlugin extends AbstractGatewayPlugin {

    public static final String NAME = "kkrpc";
    private String prefix = GATEWAY_PREFIX + "/" + NAME + "/";

    @Autowired
    RegistryCenter rc;

    LoadBalancer<InstanceMeta> loadBalancer = new RoundRibonLoadBalancer();

    @Override
    public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {
        System.out.println(" ===>>> KN gateway knrepc plugin ...");

        // 1. 通过请求路径获取服务名
        final String service = exchange.getRequest().getPath().value().substring(prefix.length());
        System.out.println(service);
        ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
                .app("app1").env("dev").namespace("public").build();
        // 2. 通过rc拿到所有活着的服务实例
        final List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);

        // 3. 先简化处理，获取第一个实例的url
        final InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
        System.out.println("inst size = " + instanceMetas.size() + ", inst " + instanceMeta);
        final String url = instanceMeta.toUrl();

        // 4. 拿到请求报文
        final Flux<DataBuffer> requestBody = exchange.getRequest().getBody();
        // 5. 通过webclient发送post请求
        WebClient webClient = WebClient.create(url);
        final Mono<ResponseEntity<String>> entity = webClient.post()
                .header("Content-Type", "application/json")
                .body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);

        // 6. 通过entity获取响应报文
        final Mono<String> body = entity.map(ResponseEntity::getBody);

        // 7. 组装响应报文
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("kn.gw.version", "v1.0.0");
        exchange.getResponse().getHeaders().add("kn.gw.plugin", getName());

        return body.flatMap(x -> exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
                .then(chain.handle(exchange));
    }

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
