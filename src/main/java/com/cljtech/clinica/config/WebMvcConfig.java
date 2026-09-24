package com.cljtech.clinica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * - Prefixa todos os {@link RestController} com {@code /api}
 *   (substitui o antigo {@code server.servlet.context-path=/api}).
 * - Serve o SPA Angular de {@code classpath:/static} e faz fallback para
 *   {@code index.html} em rotas sem extensão (deep links do Angular Router).
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        if (isApiOrInfraPath(resourcePath)) {
                            return null;
                        }

                        Resource requested = location.createRelative(resourcePath);
                        if (requested.exists() && requested.isReadable()) {
                            return requested;
                        }

                        // Deep link do Angular (ex.: /agenda) → index.html
                        if (!resourcePath.contains(".")) {
                            Resource index = new ClassPathResource("/static/index.html");
                            if (index.exists() && index.isReadable()) {
                                return index;
                            }
                        }
                        return null;
                    }
                });
    }

    private static boolean isApiOrInfraPath(String resourcePath) {
        return resourcePath.startsWith("api/")
                || resourcePath.equals("api")
                || resourcePath.startsWith("actuator/")
                || resourcePath.startsWith("v3/")
                || resourcePath.startsWith("swagger-ui");
    }
}
