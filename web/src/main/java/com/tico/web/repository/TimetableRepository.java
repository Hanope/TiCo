package com.tico.web.repository;

import com.tico.web.domain.timetable.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

}
