package bankapp.config;

import bankapp.service.CustomerService;
import bankapp.service.CustomerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class ServiceConfig {
    @Bean
    public CustomerService customerService() {
        return new CustomerServiceImpl();
    }

    private static final Logger log = LoggerFactory.getLogger(ServiceConfig.class);

    // below are for ehcache 2
//    @Bean
//    @Profile("test")
//    public CacheManager concurrentMapCacheManager() {
//        log.debug("Cache manager is concurrentMapCacheManager");
//        return new ConcurrentMapCacheManager("movieFindCache");
//    }
//
//    @Bean
//    @Profile("dev")
//    public CacheManager cacheManager() {
//        log.debug("Cache manager is ehCacheCacheManager");
//        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
//    }
//
//    @Bean
//    @Profile("dev")
//    public EhCacheManagerFactoryBean ehCacheCacheManager() {
//        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
//        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        cmfb.setShared(true);
//        return cmfb;
//    }

}
