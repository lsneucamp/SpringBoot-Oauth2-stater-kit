package co.lsnbox.auth.services;


import co.lsnbox.auth.models.Account;
import co.lsnbox.auth.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Inject
	private AccountRepository accountRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String email) {
		log.debug("Authenticating {}", email);
		String lowercaseEmail = email.toLowerCase();
		Optional<Account> userFromDatabase = accountRepository.findByEmail(lowercaseEmail);
		return userFromDatabase.map(user -> {
            List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.name()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseEmail,
                    user.getPassword(),
                    grantedAuthorities);
		}).orElseThrow(() -> new UsernameNotFoundException("Account " + lowercaseEmail + " was not found in the database"));
	}
}