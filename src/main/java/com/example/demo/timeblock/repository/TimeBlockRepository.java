package com.example.demo.timeblock.repository;

import com.example.demo.timeblock.entity.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {

	List<TimeBlock> findAllByUserIdAndDateOrderByStartTimeAsc(String userId, LocalDate date);

	@Query("""
			select case when count(b) > 0 then true else false end
			from TimeBlock b
			where b.userId = :userId
			  and b.date = :date
			  and (:excludeId is null or b.id <> :excludeId)
			  and b.startTime < :end
			  and b.endTime > :start
			""")
	boolean existsConflictingBlock(@Param("userId") String userId,
									@Param("date") LocalDate date,
									@Param("start") LocalTime start,
									@Param("end") LocalTime end,
									@Param("excludeId") Long excludeId);
}
