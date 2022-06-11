package com.mymyeong.codetest.userpoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, String> {

	/**
	 * 사용자 포인트 조회
	 */
	@Query(value = "SELECT sum(p.pointAmount) FROM UserPoint p WHERE p.userNo = :userNo")
	BigDecimal getUserPoint(@Param(value = "userNo") Long userNo);

	/**
	 * 사용자의 모든 포인트 내역 조회
	 */
	@Query(value = "SELECT p FROM UserPoint p WHERE p.userNo = :userNo order by p.processDate")
	Page<UserPoint> findAllByUserNoOrderByProcessDate(@Param(value = "userNo") Long userNo, Pageable pageable);

	@Query(value = "SELECT p FROM UserPoint p WHERE p.userNo = :userNo AND p.processDate <= :date")
	List<UserPoint> findExpiredUserPointList(@Param(value = "userNo") Long userNo, LocalDateTime date);
}
