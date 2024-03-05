package org.edupoll.app.service;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Friend;
import org.edupoll.app.repository.FriendRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendService {

	private final FriendRepository friendRepository;

	public void saveFriend(Account account, Account found) {
		Friend friend = Friend.builder() //
				.account(account) //
				.friend(found) //
				.status(false) //
				.build();
		friendRepository.save(friend);
	}

	@Transactional
	public Friend acceptRequest(Friend friend) {
		friend.setStatus(true);
		friendRepository.save(friend);
		Friend reverse = Friend.builder() //
				.account(friend.getFriend()) //
				.friend(friend.getAccount()) //
				.status(true) //
				.build();
		friendRepository.save(reverse);
		return reverse;
	}
	
	public void rejectRequest(Friend friend) {
		friendRepository.delete(friend);
	}
	
	public void deleteFriends(Friend friend) {
		friendRepository.delete(friend);
		Friend reverse = friendRepository.findByAccountAndFriend(friend.getFriend(), friend.getAccount());
		friendRepository.delete(reverse);
	}
}
