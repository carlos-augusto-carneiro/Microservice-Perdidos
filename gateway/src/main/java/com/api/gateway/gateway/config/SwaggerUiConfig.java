package com.api.gateway.gateway.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuração do Swagger UI para agregação de microserviços
 */
@Configuration
public class SwaggerUiConfig {

    private final DiscoveryClient discoveryClient;

    public SwaggerUiConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties() {
        SwaggerUiConfigProperties config = new SwaggerUiConfigProperties();
        
        Set<SwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
        
        // Adiciona o próprio Gateway
        urls.add(new SwaggerUiConfigProperties.SwaggerUrl("Gateway", "/v3/api-docs", "Gateway APIs"));
        
        // Adiciona microserviços usando as rotas do swagger proxy
        urls.add(new SwaggerUiConfigProperties.SwaggerUrl("Usuarios", "/api/swagger/usuarios-services/v3/api-docs", "Usuários Service"));
        urls.add(new SwaggerUiConfigProperties.SwaggerUrl("Email", "/api/swagger/email-services/v3/api-docs", "Email Service"));
        urls.add(new SwaggerUiConfigProperties.SwaggerUrl("Item", "/api/swagger/item-services/v3/api-docs", "Item Service"));
        
        // Tenta descobrir dinamicamente outros serviços do Eureka
        try {
            List<String> services = discoveryClient.getServices();
            for (String serviceId : services) {
                if (!serviceId.equalsIgnoreCase("recupera-item-gateway") 
                    && !serviceId.equalsIgnoreCase("eureka-server")
                    && !serviceId.contains("usuarios-services")
                    && !serviceId.contains("email-services")) {
                    
                    String url = "/" + serviceId.toLowerCase() + "/v3/api-docs";
                    String displayName = serviceId.replace("-", " ").toUpperCase();
                    urls.add(new SwaggerUiConfigProperties.SwaggerUrl(serviceId, url, displayName));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao descobrir serviços via Eureka: " + e.getMessage());
        }
        
        config.setUrls(urls);
        
        return config;
    }
}
