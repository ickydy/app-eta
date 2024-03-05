package org.edupoll.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.edupoll.app.command.AddPostCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Board;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.RecommendLog;
import org.edupoll.app.repository.BoardRepository;
import org.edupoll.app.repository.ImageRepository;
import org.edupoll.app.repository.PostRepository;
import org.edupoll.app.repository.RecommendLogRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final RecommendLogRepository recommendLogRepository;
	private final ImageRepository imageRepository;

	public Post savePost(AddPostCommand cmd, Account account) {
		Optional<Board> optional = boardRepository.findById(cmd.getBoardId());
		if (optional.isEmpty()) {
			return null;
		}
		Board board = optional.get();
		Post post = Post.builder() //
				.board(board) //
				.account(account) //
				.title(cmd.getTitle()) //
				.content(cmd.getContent()) //
				.writeAt(LocalDateTime.now()) //
				.recommend(0) //
				.comments(null) //
				.build();
		postRepository.save(post);
		return post;
	}

	@Transactional
	public Post updateRecommend(Post post, Account account) {
		post.setRecommend(post.getRecommend() + 1);
		postRepository.save(post);
		
		RecommendLog recommendLog = RecommendLog.builder() //
				.account(account) //
				.post(post) //
				.build();
		recommendLogRepository.save(recommendLog);
		return post;
	}
	
	@Transactional
	public void deletePost(Post post) {
		recommendLogRepository.deleteByPost(post);
		imageRepository.deleteByPost(post);
		postRepository.delete(post);
	}
}
