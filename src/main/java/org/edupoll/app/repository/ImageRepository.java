package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Image;
import org.edupoll.app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
	public List<Image> findByPost(Post post);

	public void deleteByPost(Post post);
}
