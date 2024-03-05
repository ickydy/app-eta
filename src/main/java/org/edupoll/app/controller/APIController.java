package org.edupoll.app.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.edupoll.app.command.AddFriendCommand;
import org.edupoll.app.command.AddRecommendCommand;
import org.edupoll.app.command.AddScheduleCommand;
import org.edupoll.app.command.DeleteScheduleCommand;
import org.edupoll.app.command.UpdateFriendCommand;
import org.edupoll.app.data.SubjectData;
import org.edupoll.app.data.TimeplaceData;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Friend;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.RecommendLog;
import org.edupoll.app.entity.Subject;
import org.edupoll.app.entity.Timeplace;
import org.edupoll.app.entity.Timetable;
import org.edupoll.app.entity.TimetableSubject;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.FriendRepository;
import org.edupoll.app.repository.PostRepository;
import org.edupoll.app.repository.RecommendLogRepository;
import org.edupoll.app.repository.SubjectRepository;
import org.edupoll.app.repository.TimeplaceRepository;
import org.edupoll.app.repository.TimetableRepository;
import org.edupoll.app.repository.TimetableSubjectRepository;
import org.edupoll.app.service.AccountService;
import org.edupoll.app.service.FriendService;
import org.edupoll.app.service.MailService;
import org.edupoll.app.service.PostService;
import org.edupoll.app.service.TimetableSubjectService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class APIController {

	private final AccountRepository accountRepository;
	private final AccountService accountService;
	private final MailService mailService;
	private final SubjectRepository subjectRepository;
	private final TimeplaceRepository timeplaceRepository;
	private final TimetableRepository timetableRepository;
	private final TimetableSubjectService timetableSubjectService;
	private final TimetableSubjectRepository timetableSubjectRepository;
	private final PostRepository postRepository;
	private final PostService postService;
	private final RecommendLogRepository recommendLogRepository;
	private final FriendRepository friendRepository;
	private final FriendService friendService;
	private final SimpMessagingTemplate simpMessagingTemplate;

	@GetMapping("/mail/register/{email}")
	public Map<String, Object> handleAuthenticationCodeMail(@PathVariable String email, HttpSession session) {
		Map<String, Object> map = new LinkedHashMap<>();

		Account account = accountRepository.findByEmail(email);

		if (account != null) {
			map.put("result", false);
			map.put("cause", "이미 사용중인 이메일입니다.");
			return map;
		}

		Random random = new Random();
		int authenticationCode = 100000 + random.nextInt(900000);

		mailService.sendAuthenticationCode(email, authenticationCode);

		session.setAttribute("authenticationCode", authenticationCode);

		map.put("result", true);
		map.put("authenticationCode", authenticationCode);
		return map;
	}

	@GetMapping("/idcheck/register/{username}")
	public Map<String, Object> handleUsernameDuplicateCheck(@PathVariable String username) {
		Map<String, Object> map = new LinkedHashMap<>();
		Optional<Account> optional = accountRepository.findByUsername(username);
		if (optional.isEmpty()) {
			map.put("result", true);
		} else {
			map.put("result", false);
		}
		return map;
	}

	@GetMapping("/timetable/subject")
	public List<SubjectData> handleAllSubjects(@RequestParam(required = false, defaultValue = "1") Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 30, Sort.by(Order.asc("id")));

		List<Subject> subjects = subjectRepository.findAll(pageRequest).getContent();

		List<SubjectData> subjectDatas = subjects.stream().map(s -> {
			List<Timeplace> timeplaces = timeplaceRepository.findBySubjectId(s.getId());

			List<TimeplaceData> timeplaceDatas = timeplaces.stream().map(timeplace -> {
				TimeplaceData timeplaceData = TimeplaceData.builder() //
						.day(timeplace.getDay()) //
						.start(timeplace.getStart()) //
						.end(timeplace.getEnd()) //
						.build();
				return timeplaceData;
			}).toList();

			SubjectData subjectData = SubjectData.builder() //
					.id(s.getId()) //
					.code(s.getCode()) //
					.name(s.getName()) //
					.professor(s.getProfessor()) //
					.type(s.getType()) //
					.grade(s.getGrade()) //
					.time(s.getTime()) //
					.place(s.getPlace()) //
					.credit(s.getCredit()) //
					.popular(s.getPopular()) //
					.target(s.getTarget()) //
					.lectureId(s.getLectureId()) //
					.lectureRate(s.getLectureRate()) //
					.theory(s.getTheory()) //
					.practice(s.getPractice()) //
					.notice(s.getNotice()) //
					.timeplaceDatas(timeplaceDatas) //
					.build();

			return subjectData;
		}).toList();

		return subjectDatas;
	}

	@GetMapping("/timetable/subject/{timetableId}")
	public List<SubjectData> handleTimetableSubject(@PathVariable Long timetableId) {

		Timetable timetable = timetableRepository.findById(timetableId).get();

		List<SubjectData> subjectDatas = timetable.getTimetableSubjects().stream().map(t -> {
			Subject subject = t.getSubject();

			List<Timeplace> timeplaces = timeplaceRepository.findBySubjectId(subject.getId());

			List<TimeplaceData> timeplaceDatas = timeplaces.stream().map(timeplace -> {
				TimeplaceData timeplaceData = TimeplaceData.builder() //
						.day(timeplace.getDay()) //
						.start(timeplace.getStart()) //
						.end(timeplace.getEnd()) //
						.build();
				return timeplaceData;
			}).toList();

			SubjectData subjectData = SubjectData.builder() //
					.id(subject.getId()) //
					.code(subject.getCode()) //
					.name(subject.getName()) //
					.professor(subject.getProfessor()) //
					.type(subject.getType()) //
					.grade(subject.getGrade()) //
					.time(subject.getTime()) //
					.place(subject.getPlace()) //
					.credit(subject.getCredit()) //
					.popular(subject.getPopular()) //
					.target(subject.getTarget()) //
					.lectureId(subject.getLectureId()) //
					.lectureRate(subject.getLectureRate()) //
					.theory(subject.getTheory()) //
					.practice(subject.getPractice()) //
					.notice(subject.getNotice()) //
					.timeplaceDatas(timeplaceDatas) //
					.timetableSubjectId(t.getId()) //
					.build();

			return subjectData;

		}).toList();

		return subjectDatas;
	}

	@PostMapping("/timetable/addSchedule/{timetableId}")
	public Map<String, Object> handleAddSchedule(@PathVariable Long timetableId, AddScheduleCommand cmd) {
		Map<String, Object> response = new LinkedHashMap<>();
		Optional<Subject> optional = subjectRepository.findById(cmd.getSubjectId());
		if (optional.isEmpty()) {
			response.put("result", false);
			return response;
		}
		Subject target = optional.get();

		Timetable timetable = timetableRepository.findById(timetableId).get();
		boolean result = timetable.getTimetableSubjects().stream().allMatch(timetableSubject -> {

			Subject subject = timetableSubject.getSubject();
			if (subject.getLectureId().equals(target.getLectureId())) {
				return false;
			}
			List<Timeplace> subjectTimeplaces = timeplaceRepository.findBySubjectId(subject.getId());
			return subjectTimeplaces.stream().allMatch(s -> {
				List<Timeplace> targetTimeplaces = timeplaceRepository.findBySubjectId(target.getId());

				return targetTimeplaces.stream().allMatch(t -> {
					if (s.getDay().equals(t.getDay())) {
						if (s.getStart() >= t.getEnd() || s.getEnd() <= t.getStart()) {
							return true;
						} else {
							return false;
						}
					}
					return true;
				});
			});
		});

		if (result) {
			Integer timetableSubjectId = timetableSubjectService.saveNewSchedule(timetable, target);
			response.put("timetableSubjectId", timetableSubjectId);
		}

		response.put("result", result);

		return response;
	}

	@PostMapping("/timetable/deleteSchedule/{timetableId}")
	public Map<String, Object> handleDeleteSchedule(@PathVariable Long timetableId, DeleteScheduleCommand cmd) {
		Map<String, Object> response = new LinkedHashMap<>();
		Optional<TimetableSubject> optional = timetableSubjectRepository.findById(cmd.getTimetableSubjectId());
		if (optional.isEmpty()) {
			response.put("result", false);
			return response;
		}
		TimetableSubject timetableSubject = optional.get();
		response.put("originSubject", timetableSubject.getSubject());
		Timetable timetable = timetableRepository.findById(timetableId).get();
		timetableSubjectService.deleteSchedule(timetable, timetableSubject);
		response.put("result", true);

		return response;
	}

	@PostMapping("/post/addRecommend")
	public Map<String, Object> handleAddRecommend(@AuthenticationPrincipal(expression = "account") Account account,
			AddRecommendCommand cmd) {
		Map<String, Object> response = new LinkedHashMap<>();
		Optional<Post> postOptional = postRepository.findById(cmd.getPostId());
		if (postOptional.isEmpty()) {
			response.put("result", false);
			response.put("cause", "존재하지 않는 게시물입니다.");
			return response;
		}
		Post post = postOptional.get();
		RecommendLog recommendLog = recommendLogRepository.findByPostAndAccount(post, account);
		if (recommendLog != null) {
			response.put("result", false);
			response.put("cause", "이미 공감한 게시물입니다.");
			return response;
		}

		post = postService.updateRecommend(post, account);
		response.put("result", true);
		response.put("recommend", post.getRecommend());
		return response;
	}

	@PostMapping("/friend/addFriend")
	public Map<String, Object> handleAddFriend(@AuthenticationPrincipal(expression = "account") Account account,
			@ModelAttribute AddFriendCommand cmd) {
		Map<String, Object> response = new LinkedHashMap<>();
		Account found = accountRepository.findByEmailOrUsername(cmd.getKeyword(), cmd.getKeyword());
		if (found == null) {
			response.put("result", false);
			response.put("cause", "올바르지 않은 상대입니다.");
			return response;
		} else if (found.equals(account)) {
			response.put("result", false);
			response.put("cause", "본인에게는 친구요청을 보낼 수 없습니다.");
			return response;
		}
		Friend friend = friendRepository.findByAccountAndFriend(account, found);
		if (friend != null) {
			response.put("result", false);
			response.put("cause", "이미 친구이거나 요청을 보낸상태입니다.");
			return response;
		}

		friendService.saveFriend(account, found);
		response.put("result", true);

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("type", "friendRequest");
		payload.put("account", account);
		simpMessagingTemplate.convertAndSend("/push/" + found.getId(), payload);

		return response;
	}

	@PostMapping("/friend/updateFriend")
	public Map<String, Object> handleUpdateFriend(@AuthenticationPrincipal(expression = "account") Account account,
			@ModelAttribute UpdateFriendCommand cmd) {
		Map<String, Object> response = new LinkedHashMap<>();
		Optional<Friend> optional = friendRepository.findById(cmd.getFriendId());
		if (optional.isEmpty()) {
			response.put("result", false);
			response.put("cause", "존재하지 않는 요청입니다.");
			return response;
		}

		Friend friend = optional.get();
		if (cmd.getStatus().equals("accept")) {
			Friend result = friendService.acceptRequest(friend);
			response.put("friend", result);
		} else if (cmd.getStatus().equals("reject")) {
			friendService.rejectRequest(friend);
		}

		response.put("result", true);
		return response;
	}

	@GetMapping("/mail/forgotId/{email}")
	public Map<String, Object> handleForgotIdMail(@PathVariable String email, HttpSession session) {
		Map<String, Object> response = new LinkedHashMap<>();

		Account account = accountRepository.findByEmail(email);

		if (account == null) {
			response.put("result", false);
			response.put("cause", "등록되지 않은 이메일입니다.");
			return response;
		}

		mailService.sendForgotId(email, account);

		response.put("result", true);
		return response;
	}

	@GetMapping("/mail/forgotPassword/{email}")
	public Map<String, Object> handleForgotIdMail(@PathVariable String email, @RequestParam String username,
			HttpSession session) {
		Map<String, Object> response = new LinkedHashMap<>();

		Optional<Account> optional = accountRepository.findByUsername(username);
		if (optional.isEmpty()) {
			response.put("result", false);
			response.put("cause", "등록되지 않은 아이디입니다.");
			return response;
		}

		Account account = optional.get();

		if (!account.getEmail().equals(email)) {
			response.put("result", false);
			response.put("cause", "해당 아이디로 사용중인 이메일과 일치하지 않습니다.");
			return response;
		}

		String tempPassword = UUID.randomUUID().toString().substring(24);
		
		accountService.updatePasswordToTempPassword(tempPassword, account);
		
		mailService.sendTempPassword(email, tempPassword);

		response.put("result", true);
		return response;
	}
}
