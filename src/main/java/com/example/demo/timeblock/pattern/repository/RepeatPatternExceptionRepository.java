package com.example.demo.timeblock.pattern.repository;

import com.example.demo.timeblock.pattern.entity.RepeatPattern;
import com.example.demo.timeblock.pattern.entity.RepeatPatternException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RepeatPatternExceptionRepository extends JpaRepository<RepeatPatternException, Long> {

	boolean existsByPatternAndDate(RepeatPattern pattern, LocalDate date);

	Optional<RepeatPatternException> findByPatternAndDate(RepeatPattern pattern, LocalDate date);
}
