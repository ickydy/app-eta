package org.edupoll.app.controller;

import java.util.List;
import java.util.Optional;

import org.edupoll.app.command.AddPostCommand;
import org.edupoll.app.command.DeletePostCommand;
import org.edupoll.app.data.BoardData;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Board;
import org.edupoll.app.entity.Image;
import org.edupoll.app.entity.Kind;
import org.edupoll.app.entity.Post;
import org.edupoll.app.repository.BoardRepository;
import org.edupoll.app.repository.ImageRepository;
import org.edupoll.app.repository.KindRepository;
import org.edupoll.app.repository.PostRepository;
import org.edupoll.app.service.MultipartFileService;
import org.edupoll.app.service.PostService;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
@RequestMapping
@RequiredArgsConstructor
public class PostController {

	private final PostRepository postRepository;
	private final PostService postService;
	private final BoardRepository boardRepository;
	private final KindRepository kindRepository;
	private final MultipartFileService multipartFileService;
	private final ImageRepository imageRepository;

	@PostMapping("/post")
	public String proceedAddPost(@AuthenticationPrincipal(expression = "account") Account account,
			@ModelAttribute AddPostCommand cmd) {

		if (cmd.getTitle() == null || cmd.getContent() == null || cmd.getTitle().trim().equals("")
				|| cmd.getContent().trim().equals("")) {
			return "redirect:/" + cmd.getBoardId();
		}

		Post post = postService.savePost(cmd, account);
		if (cmd.getImages().size() > 0) {
			multipartFileService.saveMultipartFiles(cmd.getImages(), account, post);
		}

		return "redirect:/" + cmd.getBoardId() + "/v/" + post.getId();
	}

	@DeleteMapping("/post/delete")
	public String proceedDelete(@AuthenticationPrincipal(expression = "account") Account account,
			@ModelAttribute DeletePostCommand cmd) {
		Optional<Post> optional = postRepository.findById(cmd.getPostId());
		if (optional.isEmpty()) {
			return "redirect:/";
		}
		Post post = optional.get();
		if (!post.getAccount().getId().equals(account.getId())) {
			return "redirect:/";
		}
		Integer boardId = post.getBoard().getId();
		postService.deletePost(post);
		return "redirect:/" + boardId;
	}

	@GetMapping("/{boardId}/v/{postId}")
	public String showPost(@PathVariable Integer boardId, @PathVariable Long postId, Model model) {
		Optional<Board> boardOptional = boardRepository.findById(boardId);
		Optional<Post> postOptional = postRepository.findById(postId);
		if (boardOptional == null || postOptional == null) {
			return "redirect:/";
		}

		Post post = postOptional.get();
		model.addAttribute("post", post);

		List<Image> images = imageRepository.findByPost(post);
		model.addAttribute("images", images);

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

		return "post/post-home";
	}

}
