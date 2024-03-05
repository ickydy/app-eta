package org.edupoll.app.service;

import org.edupoll.app.command.RegisterCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	public void saveAccount(RegisterCommand cmd, Account account) {
		account.setUsername(cmd.getUsername());
		account.setNickname(cmd.getNickname());

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode(cmd.getPassword());
		account.setPassword("{bcrypt}" + encoded);
		
		accountRepository.save(account);
	}
	
	public void updatePasswordToTempPassword(String tempPassword, Account account) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode(tempPassword);
		account.setPassword("{bcrypt}" + encoded);
		accountRepository.save(account);
	}
}
