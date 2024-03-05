package org.edupoll.app.service;

import java.time.LocalDateTime;

import org.edupoll.app.command.AddCommentCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Comment;
import org.edupoll.app.entity.Post;
import org.edupoll.app.repository.BoardRepository;
import org.edupoll.app.repository.CommentRepository;
import org.edupoll.app.repository.PostRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final BoardRepository boardRepository;
	private final PostRepository postRepository;

	public Comment saveComment(AddCommentCommand cmd, Account account) {
		Post post = postRepository.findById(cmd.getPostId()).get();
		Comment comment = Comment.builder() //
				.account(account) //
				.content(cmd.getContent()) //
				.post(post) //
				.writeAt(LocalDateTime.now()) //
				.recommend(0) //
				.build();
		commentRepository.save(comment);
		return comment;
	}
}
