package org.edupoll.app.repository;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.RecommendLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendLogRepository extends JpaRepository<RecommendLog, Long> {
	public RecommendLog findByPostAndAccount(Post post, Account account);

	public void deleteByPost(Post post);
}
