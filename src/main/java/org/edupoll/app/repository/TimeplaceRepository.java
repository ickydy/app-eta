package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Timeplace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeplaceRepository extends JpaRepository<Timeplace, Integer> {

	public List<Timeplace> findBySubjectId(Integer subjectId);
}
