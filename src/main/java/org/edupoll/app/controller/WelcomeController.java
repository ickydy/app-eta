package org.edupoll.app.controller;

import java.util.List;

import org.edupoll.app.data.BoardData;
import org.edupoll.app.data.PostData;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Board;
import org.edupoll.app.entity.Kind;
import org.edupoll.app.entity.Post;
import org.edupoll.app.repository.BoardRepository;
import org.edupoll.app.repository.KindRepository;
import org.edupoll.app.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WelcomeController {

	private final BoardRepository boardRepository;
	private final KindRepository kindRepository;
	private final PostRepository postRepository;

	@ModelAttribute(name = "controller")
	public String currentController() {
		return "welcomeController";
	}

	@GetMapping({ "/", "/index" })
	public String showIndex(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		if (account == null) {
			return "welcome/index-home";
		}

		List<Kind> kinds = kindRepository.findAll(Sort.by(Order.asc("id")));
		List<Board> allBoards = boardRepository.findAll(Sort.by(Order.asc("id")));
		List<BoardData> boardDatas = kinds.stream().map(kind -> {
			List<Board> boards = allBoards.stream().filter(board -> board.getKind() == kind).toList();
			BoardData boardData = BoardData.builder().kind(kind).boards(boards).build();
			return boardData;
		}).toList();

		model.addAttribute("boardDatas", boardDatas);
		model.addAttribute("account", account);

		List<PostData> postDatas = allBoards.stream().map(board -> {
			List<Post> posts = postRepository.findTop4ByBoardOrderByWriteAtDesc(board);
			PostData postData = PostData.builder() //
					.board(board) //
					.posts(posts) //
					.build();
			return postData;
		}).toList();
		model.addAttribute("postDatas", postDatas);

		List<Post> currentPosts = postRepository.findTop4ByOrderByWriteAtDesc();
		model.addAttribute("currentPosts", currentPosts);

		return "welcome/index";
	}

}
