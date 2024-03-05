package org.edupoll.app.repository;

import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
	public Optional<Account> findByUsername(String username);
	
	public Account findByEmail(String email);
	
	public Account findByEmailOrUsername(String keyword1, String keyword2);
}
