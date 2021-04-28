// spring-boot-angular-scaffolding/backend/src/main/java/in/keepgrowing/springbootangularscaffolding/config/DevCorsConfiguration.java
package com.zhaohu.compare.permissions.config;
 
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@Configuration
@Profile("development")
public class CorsConfiguration implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	System.out.println("============cors config ===============");
        registry.addMapping("/**").allowedOrigins("http://localhost:4200")
        		.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    }
}