package web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import container.xml.XmlHelper;
import web.authorization.HTTPBasicAuthorizeAttribute;


@Configuration
@SpringBootApplication
@ComponentScan(basePackages = {"web.controller", "web.swagger", "container.xml"})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // TODO Auto-generated method stub
        return builder.sources(Application.class);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        HTTPBasicAuthorizeAttribute httpBasicFilter = new HTTPBasicAuthorizeAttribute();
        registrationBean.setFilter(httpBasicFilter);
        List<String> urlPatterns = new ArrayList<>();
        
        XmlHelper xmlHelper = new XmlHelper();
        List<String> cores = xmlHelper.getAllCore();
        for(String core : cores){
        	urlPatterns.add("/" + core + "/*");
        }
        
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }     

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
