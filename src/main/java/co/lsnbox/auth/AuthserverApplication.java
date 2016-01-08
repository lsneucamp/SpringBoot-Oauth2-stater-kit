package co.lsnbox.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@SpringBootApplication
@EnableResourceServer
public class AuthserverApplication implements CommandLineRunner {

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		this.environment.getActiveProfiles();
		if (this.environment.getActiveProfiles().length>0) {
			System.out.println("Current Active profile:" + this.environment.getActiveProfiles()[0]);
		} else{
			System.out.println("Default Active profile is running");

		}
//		SecurityContextHolder.getContext().setAuthentication(
//				new AnonymousAuthenticationToken("anonymous", "N/A", AuthorityUtils
//						.commaSeparatedStringToAuthorityList(Role.ANONYMOUS.name())));
	}

	@Bean
	public Validator validator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		return validator;
	}



}
