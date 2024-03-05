package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
	public List<Friend> findByAccountAndStatus(Account account, Boolean status);

	public List<Friend> findByFriendAndStatus(Account account, Boolean status);
	
	public Friend findByAccountAndFriend(Account account1, Account account2);
}
