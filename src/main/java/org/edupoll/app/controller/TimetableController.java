package org.edupoll.app.controller;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.command.AddTimetableCommand;
import org.edupoll.app.command.DeleteTimetableCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Timetable;
import org.edupoll.app.repository.TimetableRepository;
import org.edupoll.app.service.TimetableService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/timetable")
@RequiredArgsConstructor
public class TimetableController {

	private final TimetableRepository timetableRepository;
	private final TimetableService timetableService;

	@ModelAttribute(name = "controller")
	public String currentController() {
		return "timetableController";
	}

	@GetMapping
	public String showTimetable(@AuthenticationPrincipal(expression = "account") Account account, Model model) {

		List<Timetable> timetables = timetableRepository.findByAccount(account);
		if (timetables.size() == 0 || timetables == null) {
			Timetable timetable = timetableService.createNewTimetableAuto(account);
			timetables.add(timetable);
			model.addAttribute("timetable", timetable);
		} else {
			model.addAttribute("timetable", timetables.get(0));
		}

		model.addAttribute("timetables", timetables);

		return "timetable/timetable";
	}

	@GetMapping("/{timetableId}")
	public String showSpecificTimetable(@AuthenticationPrincipal(expression = "account") Account account,
			@PathVariable Long timetableId, Model model) {

		Optional<Timetable> optional = timetableRepository.findById(timetableId);
		if (optional.isEmpty()) {
			return "redirect:/timetable";
		}
		Timetable timetable = optional.get();

		List<Timetable> timetables = timetableRepository.findByAccount(account);

		model.addAttribute("timetables", timetables);
		model.addAttribute("timetable", timetable);

		return "timetable/timetable";
	}

	@PostMapping("/newTimetable")
	public String proceedAddTimetable(@AuthenticationPrincipal(expression = "account") Account account,
			@ModelAttribute AddTimetableCommand cmd) {

		String name = cmd.getName();

		if (name == null || name.trim() == "") {
			return "redirect:/timetable";
		}

		Timetable timetable = timetableService.createNewTimetable(name, account);
		return "redirect:/timetable/" + timetable.getId();
	}

	@DeleteMapping("/delete")
	public String proceedDeleteTimetable(@AuthenticationPrincipal(expression = "account") Account account,
			@ModelAttribute DeleteTimetableCommand cmd) {
		Timetable table = timetableRepository.findById(cmd.getTimetableId()).get();
		boolean result = timetableService.deleteTimetable(table, account);
		if (!result) {
			return "redirect:/timetable/" + table.getId();
		}
		return "redirect:/timetable";
	}

}
