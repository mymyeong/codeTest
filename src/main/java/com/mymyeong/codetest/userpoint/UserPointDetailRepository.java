package com.mymyeong.codetest.userpoint;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointDetailRepository extends JpaRepository<UserPointDetail, Long> {

	/**
	 * 현제 NO SEQ 조회
	 */
	@Query(value = "SELECT USER_POINT_DETAIL_SEQ_GENERATOR.nextval", nativeQuery = true)
	Long getUserPointDetailSeq();

	@Modifying
	@Query(value = "INSERT INTO User_Point_Detail (no) SELECT :#{#userPointDetail.no} ", nativeQuery = true)
	UserPointDetail saveUserPointDetail(@Param(value = "userPointDetail") UserPointDetail userPointDetail);

	/**
	 * 사용자의 가용 포인트 상세 내역 조회
	 * 
	 * @param userNo
	 * @param dateTime
	 * @return
	 */
	@Query(value = "SELECT d.pointDetailNo as pointDetailNo, sum(d.pointAmount) as pointAmount, min(d.processDate) as processDate " //
			+ "FROM UserPointDetail d " //
			+ "WHERE d.processDate >= :dateTime " //
			+ "AND d.userNo = :userNo " //
			+ "GROUP by d.pointDetailNo " //
			+ "HAVING sum(d.pointAmount) > 0 " //
			+ "ORDER BY min(d.processDate)")
	List<UserPointDetailUseInterface> getUserUseablePointList(@Param(value = "userNo") Long userNo, LocalDateTime dateTime);
}
