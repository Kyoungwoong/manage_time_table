package com.example.demo.timeblock.pattern.repository;

import com.example.demo.timeblock.pattern.entity.RepeatPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepeatPatternRepository extends JpaRepository<RepeatPattern, Long> {

	List<RepeatPattern> findAllByUserIdAndIsActiveTrue(String userId);

	java.util.Optional<RepeatPattern> findByIdAndUserId(Long id, String userId);
}
