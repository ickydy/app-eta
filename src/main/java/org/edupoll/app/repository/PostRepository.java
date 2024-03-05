package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Board;
import org.edupoll.app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
	public List<Post> findTop4ByBoardOrderByWriteAtDesc(Board board);
	
	public List<Post> findTop4ByOrderByWriteAtDesc();
}
