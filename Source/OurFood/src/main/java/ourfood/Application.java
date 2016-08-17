package ourfood;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;

@ComponentScan
@EnableAutoConfiguration
@Configuration
@PropertySource("classpath:application.properties")
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        // TimeZone.setDefault(TimeZone.getTimeZone("IST"));
        // TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    /**
     * Static dialect for seamless integration of CDN
     */
    @Bean
    public StaticDialect staticDialect() {
        return new StaticDialect();
    }

    /**
     * Error handler for 404 handling
     */
    // TODO check for a better way
    @Bean
    public EmbeddedServletContainerCustomizer notFound() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404.html"));
            }
        };
    }

    @Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager cacheManager = new SimpleCacheManager();        
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("applites")));
        return cacheManager;
    }

    // Uncomment to enable SSL

    /*
     * @Bean public EmbeddedServletContainerFactory servletContainer() { TomcatEmbeddedServletContainerFactory tomcat =
     * new TomcatEmbeddedServletContainerFactory() {
     * 
     * @Override protected void postProcessContext(Context context) { SecurityConstraint securityConstraint = new
     * SecurityConstraint(); securityConstraint.setUserConstraint("CONFIDENTIAL"); SecurityCollection collection = new
     * SecurityCollection(); collection.addPattern("/*"); securityConstraint.addCollection(collection);
     * context.addConstraint(securityConstraint); } };
     * 
     * tomcat.addAdditionalTomcatConnectors(initiateHttpConnector()); return tomcat; }
     * 
     * private Connector initiateHttpConnector() { Connector connector = new
     * Connector("org.apache.coyote.http11.Http11NioProtocol"); connector.setScheme("http"); connector.setPort(8080);
     * connector.setSecure(false); connector.setRedirectPort(8443);
     * 
     * return connector; }
     */
}

// Notes:
// Application properties
// # SSL
// server.port=8443
// server.ssl.key-store=classpath:qnovon_scanon_web.p12
// server.ssl.key-store-password=com.qnovon.scanon.web@5863
// server.ssl.keyStoreType=PKCS12
// server.ssl.keyAlias=scanon_web