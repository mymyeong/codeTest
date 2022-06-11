package com.mymyeong.codetest.userpoint;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointDetailRepository extends JpaRepository<UserPointDetail, Long> {

	@Modifying
	@Transactional
	@Query(nativeQuery = true, //
			value = "INSERT INTO User_Point_Detail " //
					+ "(NO, POINT_AMOUNT, POINT_DETAIL_NO, POINT_STATUS, PROCESS_DATE, USER_NO, USER_POINT_NO) " //
					+ "VALUES " //
					+ "(:#{#userPointDetail.no}, :#{#userPointDetail.pointAmount}, :#{#userPointDetail.no}, :#{#userPointDetail.pointStatus.name}, :#{#userPointDetail.processDate}, :#{#userPointDetail.userNo}, :#{#userPointDetail.userPoint.no}) ")
	Integer saveUserPointDetail(@Param("userPointDetail") UserPointDetail userPointDetail);

	/**
	 * 사용자의 가용 포인트 상세 내역 조회
	 */
	@Query(value = "SELECT d.pointDetailNo as pointDetailNo, sum(d.pointAmount) as pointAmount, min(d.processDate) as processDate " //
			+ "FROM UserPointDetail d " //
			+ "WHERE d.processDate >= :dateTime " //
			+ "AND d.userNo = :userNo " //
			+ "GROUP by d.pointDetailNo " //
			+ "HAVING sum(d.pointAmount) > 0 " //
			+ "ORDER BY min(d.processDate)")
	List<UserPointDetailUseInterface> getUserUseablePointList(@Param(value = "userNo") Long userNo, LocalDateTime dateTime);

	/**
	 * 사용자 포인트 NO 의 상세 내역 조회
	 */
	@Query(value = "SELECT d FROM UserPointDetail d where d.userPoint.no = :userPointNo")
	List<UserPointDetail> getUserPointNoList(@Param(value = "userPointNo") Long userPointNo);
}
