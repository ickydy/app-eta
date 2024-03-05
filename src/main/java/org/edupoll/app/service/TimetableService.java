package org.edupoll.app.service;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Timetable;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.TimetableRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimetableService {

	private final TimetableRepository timetableRepository;
	private final AccountRepository accountRepository;

	public Timetable createNewTimetableAuto(Account account) {
		Timetable table = Timetable.builder().account(account).name("시간표 1").totalCredit(0).build();
		timetableRepository.save(table);
		return table;
	}

	public Timetable createNewTimetable(String name, Account account) {
		Timetable table = Timetable.builder().account(account).name(name).totalCredit(0).build();
		timetableRepository.save(table);
		return table;
	}

	public boolean deleteTimetable(Timetable timetable, Account account) {
		if (!timetable.getAccount().getId().equals(account.getId())) {
			return false;
		}
		timetableRepository.delete(timetable);
		return true;
	}
}

