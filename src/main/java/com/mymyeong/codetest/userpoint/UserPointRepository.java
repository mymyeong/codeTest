package com.mymyeong.codetest.userpoint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, Long> {

	/**
	 * 사용자 포인트 조회
	 * 
	 * @param userNo
	 * @return
	 */
	@Query(value = "SELECT sum(pointAmount) FROM UserPoint p WHERE userNo = :userNo")
	Long getUserPoint(@Param(value = "userNo") Long userNo);

	/**
	 * 사용자의 모든 포인트 내역 조회
	 * 
	 * @param userNo
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT p FROM UserPoint p WHERE p.userNo = :userNo order by p.processDate")
	Page<UserPoint> findAllByUserNoOrderByProcessDate(@Param(value = "userNo") Long userNo, Pageable pageable);

//	@Modifying
//	@Transactional
//	@Query(value = "INSERT INTO UserPointDetail " //
//			+ "(no, userNo, pointAmount, pointDetailNo, pointStatus, processDate, userPointNo) " //
//			+ "VALUES " //
//			+ "(USER_POINT_DETAIL_SEQ_GENERATOR.nextVal, :#{#userPointDetail.userNo}, :#{#userPointDetail.pointAmount}, :#{#userPointDetail.pointDetailNo}, "
//			+ ":#{#userPointDetail.pointStatus}, :#{#userPointDetail.processDate}, :#{#userPointDetail.userPointNo})", nativeQuery = true)
//	UserPointDetail saveUserPointDetail(@Param(value = "userPointDetail") UserPointDetail userPointDetail);
}
