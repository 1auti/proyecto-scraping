package com.chatbotconscraping.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImagenConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry.addResourceHandler("/imagenes-productos-jumbo/**").addResourceLocations("file:C:/Users/Lautaro/Desktop/Desarrollo-web/Proyectos/proyecto-chatbot-con-scraping/imagenes-productos-jumbo/");
    }
}
