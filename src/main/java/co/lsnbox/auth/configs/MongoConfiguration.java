package co.lsnbox.auth.configs;


import co.lsnbox.auth.configs.security.OAuth2AuthenticationReadConverter;
import com.mongodb.Mongo;
import org.mongeez.Mongeez;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoRepositories(basePackages = "co.lsnbox.auth.repositories")
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class MongoConfiguration extends AbstractMongoConfiguration {

    private final Logger log = LoggerFactory.getLogger(MongoConfiguration.class);

    @Inject
    private Mongo mongo;

    @Inject
    private Environment env;

    @Inject
    private MongoProperties mongoProperties;

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(localValidatorFactoryBean());
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongo;
    }

    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        OAuth2AuthenticationReadConverter converter = new OAuth2AuthenticationReadConverter();
        converterList.add(converter);
        return new CustomConversions(converterList);
    }

    @Bean
//    @Profile("!"+ Constants.PRODUCTION_PROFILE)
    public Mongeez mongeez() {
        log.debug("Configuring Mongeez");
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource("/mongeez/master.xml"));
        mongeez.setMongo(mongo);
        mongeez.setDbName(mongoProperties.getDatabase());
        mongeez.process();
        return mongeez;
    }
}
