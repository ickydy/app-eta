package org.edupoll.app.service;

import org.edupoll.app.entity.Subject;
import org.edupoll.app.entity.Timetable;
import org.edupoll.app.entity.TimetableSubject;
import org.edupoll.app.repository.TimetableRepository;
import org.edupoll.app.repository.TimetableSubjectRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimetableSubjectService {

	private final TimetableSubjectRepository timetableSubjectRepository;
	private final TimetableRepository timetableRepository;
	
	@Transactional
	public Integer saveNewSchedule(Timetable timetable, Subject subject) {
		TimetableSubject timetableSubject = TimetableSubject.builder() //
				.timetable(timetable) //
				.subject(subject) //
				.build();
		timetableSubjectRepository.save(timetableSubject);
		
		timetable.setTotalCredit(timetable.getTotalCredit()+subject.getCredit());
		timetableRepository.save(timetable);
		return timetableSubject.getId();
	}
	
	@Transactional
	public void deleteSchedule(Timetable timetable, TimetableSubject timetableSubject) {
		timetableSubjectRepository.delete(timetableSubject);
		timetable.setTotalCredit(timetable.getTotalCredit() - timetableSubject.getSubject().getCredit());
		timetableRepository.save(timetable);
	}
}
