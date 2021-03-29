package bankapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class DataSourceConfig {

    @Configuration
    @Profile({"jpa"})
    @PropertySource("classpath:application-h2.properties")
    public static class JpaConfig {

    }

    @Configuration
    @Profile({"mongo"})
    @PropertySource("classpath:application-mongo.properties")
    //PropertySource can't load yml, need a bit custom coding which is not default spring
    public static class MongoConfig {

    }

}
