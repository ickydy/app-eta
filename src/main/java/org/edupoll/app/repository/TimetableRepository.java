package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
	public List<Timetable> findByAccount(Account account);
}
