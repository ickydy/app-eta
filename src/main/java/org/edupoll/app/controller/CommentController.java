package org.edupoll.app.controller;

import org.edupoll.app.command.AddCommentCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/comment")
	private String proceedAddComment(@AuthenticationPrincipal(expression = "account") Account account,
			@ModelAttribute AddCommentCommand cmd) {
		if (cmd.getBoardId() == null || cmd.getPostId() == null) {
			return "redirect:/";
		} else if (cmd.getContent() == null || cmd.getContent().trim().equals("")) {
			return "redirect:/" + cmd.getBoardId() + "/v/" + cmd.getPostId();
		}
		commentService.saveComment(cmd, account);
		return "redirect:/" + cmd.getBoardId() + "/v/" + cmd.getPostId();
	}
}
