package cz.prague.vida.vocab;

import com.sun.faces.config.ConfigureListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.ServletContextAware;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
@ComponentScan("cz.prague")
public class Main implements ServletContextAware {

    public static void main(String[] args) {
        initialSetup();
        SpringApplication.run(Main.class, args);
    }

    private static void initialSetup() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
    }

    @Bean
    public ServletRegistrationBean facesServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new FacesServlet(), "*.xhtml");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        return new ServletListenerRegistrationBean<ConfigureListener>(
                new ConfigureListener());
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
    }

//    @Bean
//    ServletRegistrationBean jsfServletRegistration(ServletContext servletContext) {
//        //spring boot only works if this is set
//        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
//
//        //registration
//        ServletRegistrationBean srb = new ServletRegistrationBean();
//        srb.setServlet(new FacesServlet());
//        srb.setUrlMappings(Arrays.asList("*.xhtml"));
//        srb.setLoadOnStartup(1);
//        return srb;
//    }
}