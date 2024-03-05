package org.edupoll.app.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;import java.util.UUID;

import org.edupoll.app.command.RegisterAdmissionCommand;
import org.edupoll.app.command.RegisterCommand;
import org.edupoll.app.command.RegisterEmailCommand;
import org.edupoll.app.command.RegisterMajorCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Category;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.CategoryRepository;
import org.edupoll.app.service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

	private final CategoryRepository categoryRepository;
	private final AccountRepository accountRepository;
	private final AccountService accountService;

	@GetMapping("/login")
	public String showLogin(Authentication authentication) {
		return "account/login";
	}

	@GetMapping("/register")
	public String showRegister(Model model) {
		int currentYear = LocalDate.now().getYear();

		List<Integer> years = new ArrayList<>();
		for (int i = currentYear; i >= 1995; i--) {
			years.add(i);
		}

		model.addAttribute("years", years);

		return "account/register/admission";
	}

	@PostMapping("/register/admission")
	public String proceedSetAdmission(@ModelAttribute RegisterAdmissionCommand cmd, HttpSession session, Model model) {
		if (cmd.getAdmissionYear() == null) {
			return "redirect:/register";
		}
		Account account = Account.builder().admissionYear(cmd.getAdmissionYear()).build();
		session.setAttribute("registAccount", account);

		return "redirect:/account/register/major";
	}

	@GetMapping("/register/major")
	public String showRegisterMajor(HttpSession session, Model model) {

		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		return "account/register/major";
	}

	@PostMapping("/register/major")
	public String proceedSetMajor(@ModelAttribute RegisterMajorCommand cmd, HttpSession session, Model model) {
		if (cmd.getCategory() == null) {
			return "redirect:/account/register/admission";
		}

		Account account = (Account) session.getAttribute("registAccount");
		Category category = categoryRepository.findById(cmd.getCategory()).orElse(null);
		account.setCategory(category);
		session.setAttribute("registAccount", account);

		return "redirect:/account/register/email";
	}

	@GetMapping("/register/email")
	public String showRegisterEmail(HttpSession session, Model model) {

		return "account/register/email";
	}

	@PostMapping("/register/email")
	public String proceedSetEmail(@ModelAttribute RegisterEmailCommand cmd, HttpSession session) {
		if (cmd.getEmail() == null) {
			return "redirect:/account/register/email";
		}
		Account account = (Account) session.getAttribute("registAccount");
		account.setEmail(cmd.getEmail());
		session.setAttribute("registAccount", account);

		return "redirect:/account/register/form";
	}

	@GetMapping("/register/form")
	public String showRegisterForm(HttpSession session, Model model) {
		model.addAttribute("account", session.getAttribute("registAccount"));
		Account registerCommand = Account.builder().build();
		model.addAttribute("registerCommand", registerCommand);
		return "account/register/form";
	}

	@PostMapping("/register/form")
	public String proceedSetForm(@ModelAttribute @Valid RegisterCommand cmd, BindingResult result, HttpSession session,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("account", session.getAttribute("registAccount"));
			return "account/register/form";
		}

		Optional<Account> optional = accountRepository.findByUsername(cmd.getUsername());
		if (optional.isPresent()) {
			model.addAttribute("account", session.getAttribute("registAccount"));
			return "account/register/form";
		}
		
		Account account = (Account) session.getAttribute("registAccount");
		accountService.saveAccount(cmd, account);
		
		model.addAttribute("username", cmd.getUsername());

		return "account/login";
	}

	@GetMapping("/duplicate")
	public String proceedDuplicateLogin() {
		return "account/logout";
	}
	
	@GetMapping("/forgot/id")
	public String showForgotId() {
		return "account/forgot/id";
	}
	
	@GetMapping("/forgot/password")
	public String showForgotPassword() {
		return "account/forgot/password";
	}
}
