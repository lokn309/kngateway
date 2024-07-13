package cn.lokn.kngateway;

import cn.kimmking.kkrpc.core.api.RegistryCenter;
import cn.kimmking.kkrpc.core.registry.kk.KkRegistryCenter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * @description:
 * @author: lokn
 * @date: 2024/06/22 23:36
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RegistryCenter rc() {
        // 后期需要更换为自己的
        return new KkRegistryCenter();
    }

    @Bean
    ApplicationRunner runner(@Autowired ApplicationContext context) {
        return args -> {
            final SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);
            Properties mappings = new Properties();
            mappings.put(GatewayPlugin.GATEWAY_PREFIX + "/**", "gatewayWebHandler");
            handlerMapping.setMappings(mappings);
            // 注册
            handlerMapping.initApplicationContext();
            System.out.println("knrpc gateway start...");
        };
    }

}
