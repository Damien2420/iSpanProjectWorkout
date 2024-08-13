package com.four.service.course.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.four.model.course.schedule.Schedule;
import com.four.model.course.schedule.ScheduleRepository;
import com.four.model.memberAdm.MemberBean;
import com.four.model.memberAdm.MemberRepository;

@Service
public class ScheduleService {
	
// Member
	@Autowired private MemberRepository memRepo;
	public MemberBean checkLogin() {return memRepo.findByMemEmail("Prw@mail.com");}
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	public List<Schedule> findAll() {
		return scheduleRepository.findAll();
	}
	public Schedule findById(Integer id) {
		Optional<Schedule> optional = scheduleRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	public List<Schedule> findByCourseId(Integer id) {
		List<Schedule> schedules = scheduleRepository.findByCourseId(id);
		if(!schedules.isEmpty()) {
			return schedules;
		}
		return new ArrayList<>();
	}
	public List<Schedule> findByCourseIds(Integer[] ids) {
		return scheduleRepository.findByCourseIds(ids);
	}
	public List<Schedule> finSchedules(Integer[] ids) {
		return scheduleRepository.findByCourseIds(ids);
	}
	public void Save(Schedule schedule) {
		scheduleRepository.save(schedule);
	}
	public void Delete(Integer scheduleId) {
		scheduleRepository.deleteById(scheduleId);
	}
	public List<Schedule> findByCurrentDate(LocalDate currentDate) {
		return scheduleRepository.findByCurrentDate(currentDate);
	}
	public List<Schedule> findByPastDate(LocalDate currentDate) {
		return scheduleRepository.findByPastDate(currentDate);
	}
	public List<Schedule> findByFutureDate(LocalDate currentDate) {
		return scheduleRepository.findByFutureDate(currentDate);
	}
}
