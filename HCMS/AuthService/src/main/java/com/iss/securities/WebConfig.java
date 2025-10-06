package com.iss.securities;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    //http://localhost:9087/profile-pictures/doc1.jpeg
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // Serve files in D:/…/profile-pictures/ via URL /profile-pictures/**
        registry.addResourceHandler("/profile-pictures/**")
                .addResourceLocations("file:D:/NikhilReddy/spring and spring boot/SpringBoot_Projects/HealthCareManagementSystem/profile-pictures/");
    }
}
