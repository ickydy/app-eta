package org.edupoll.app.config.support;

import java.util.Optional;

import org.edupoll.app.common.CustomUserDetails;
import org.edupoll.app.entity.Account;
import org.edupoll.app.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final AccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> optional = accountRepository.findByUsername(username);
		
		Account account = optional.orElseThrow(() -> new UsernameNotFoundException(username));
		return new CustomUserDetails(account);
	}
}