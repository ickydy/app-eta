package org.edupoll.app.controller;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.data.BoardData;
import org.edupoll.app.entity.Board;
import org.edupoll.app.entity.Kind;
import org.edupoll.app.entity.Post;
import org.edupoll.app.repository.BoardRepository;
import org.edupoll.app.repository.KindRepository;
import org.edupoll.app.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	private final BoardRepository boardRepository;
	private final KindRepository kindRepository;
	private final PostRepository postRepository;

	@GetMapping("/{boardId}")
	public String showBoard(@PathVariable Integer boardId, Model model) {
		Optional<Board> optional = boardRepository.findById(boardId);
		if (optional.isEmpty()) {
			return "redirect:/";
		}
		Board board = optional.get();
		model.addAttribute("board", board);

		List<Kind> kinds = kindRepository.findAll(Sort.by(Order.asc("id")));
		List<Board> allBoards = boardRepository.findAll(Sort.by(Order.asc("id")));
		List<BoardData> boardDatas = kinds.stream().map(kind -> {
			List<Board> boards = allBoards.stream().filter(b -> b.getKind() == kind).toList();
			BoardData boardData = BoardData.builder().kind(kind).boards(boards).build();
			return boardData;
		}).toList();

		model.addAttribute("boardDatas", boardDatas);
		
		List<Post> currentPosts = postRepository.findTop4ByOrderByWriteAtDesc();
		model.addAttribute("currentPosts", currentPosts);

		return "board/board-home";
	}

}
