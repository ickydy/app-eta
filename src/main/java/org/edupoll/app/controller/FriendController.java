package org.edupoll.app.controller;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.command.DeleteFriendCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Friend;
import org.edupoll.app.entity.Timetable;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.FriendRepository;
import org.edupoll.app.repository.TimetableRepository;
import org.edupoll.app.service.FriendService;
import org.edupoll.app.service.TimetableService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

	private final AccountRepository accountRepository;
	private final FriendRepository friendRepository;
	private final TimetableRepository timetableRepository;
	private final TimetableService timetableService;
	private final FriendService friendService;

	@ModelAttribute(name = "controller")
	public String currentController() {
		return "friendController";
	}

	@GetMapping
	public String showFriend(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		List<Friend> friends = friendRepository.findByAccountAndStatus(account, true);
		model.addAttribute("friends", friends);

		List<Friend> requests = friendRepository.findByFriendAndStatus(account, false);
		model.addAttribute("requests", requests);

		return "friend/friend-home";
	}

	@GetMapping("/@{accountId}")
	public String showFriendTimetable(@PathVariable String accountId,
			@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		Optional<Account> optional = accountRepository.findById(accountId);
		if (optional.isEmpty()) {
			return "redirect:/friend";
		}
		Account target = optional.get();

		Friend friend = friendRepository.findByAccountAndFriend(account, target);
		if (friend == null || friend.getStatus() == false) {
			return "redirect:/friend";
		}

		List<Timetable> timetables = timetableRepository.findByAccount(target);
		if (timetables.size() == 0 || timetables == null) {
			Timetable timetable = timetableService.createNewTimetableAuto(target);
			timetables.add(timetable);
			model.addAttribute("timetable", timetable);
		} else {
			model.addAttribute("timetable", timetables.get(0));
		}
		return "friend/friend-timetable";
	}

	@PostMapping("/delete")
	public String proceedDeleteFriend(@ModelAttribute DeleteFriendCommand cmd,
			@AuthenticationPrincipal(expression = "account") Account account) {
		Optional<Account> optional = accountRepository.findById(cmd.getFriendId());
		if (optional.isEmpty()) {
			return "redirect:/friend";
		}
		Account target = optional.get();

		Friend friend = friendRepository.findByAccountAndFriend(account, target);
		friendService.deleteFriends(friend);
		return "redirect:/friend";
	}

}
