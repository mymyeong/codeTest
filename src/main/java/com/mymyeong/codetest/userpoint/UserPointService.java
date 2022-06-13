package com.mymyeong.codetest.userpoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mymyeong.codetest.util.UuidGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserPointService {

	final UserPointRepository userPointRepository;

	final UserPointDetailRepository userPointDetailRepository;

	public UserPointService(UserPointRepository userPointRepository, UserPointDetailRepository userPointDetailRepository) {
		this.userPointRepository = userPointRepository;
		this.userPointDetailRepository = userPointDetailRepository;
	}

	/**
	 * 사용자 포인트 조회
	 */
	@Transactional(readOnly = true)
	public BigDecimal getUserPoint(Long userNo) {
		// 포인트 조회
		return userPointRepository.getUserPoint(userNo);
	}

	/**
	 * 사용자 포인트 내역 조회
	 */
	@Transactional(readOnly = true)
	public List<UserPoint> getUserPointList(Long userNo, Pageable pageable) {
		Page<UserPoint> userPointList = userPointRepository.findAllByUserNoOrderByProcessDate(userNo, pageable);

		if (userPointList == null) {
			return new ArrayList<>();
		}

		return userPointList.toList();
	}

	public List<UserPointDetailUseInterface> getUsablePointList(Long userNo, Pageable pageable) {

		return userPointDetailRepository.getUserUsablePointList(userNo, LocalDateTime.now().minus(Period.ofDays(370)));
	}

	/**
	 * 포인트 충전
	 */
	@Transactional(readOnly = false)
	public void chargePoint(Long userNo, BigDecimal parseChargePointAmount) throws Exception {

		// save user Point
		UserPoint userPoint = getUserPoint(userNo, parseChargePointAmount);
		UserPoint saveUserPoint = userPointRepository.save(userPoint);
		log.info("save UserPoint : {}", saveUserPoint);

		// save user point detail
		String userPointDetailId = UuidGenerator.getUuid();
		var userPointDetail = getUserPointDetail(userNo, userPointDetailId, parseChargePointAmount, saveUserPoint);
		log.info("save userPointDetail : {}", userPointDetail);
		UserPointDetail afterUserPoint = userPointDetailRepository.save(userPointDetail);
		if (afterUserPoint == null) {
			throw new Exception("사용자 포인트 출전 실패");
		}
	}

	/**
	 * 포인트 사용
	 */
	@Transactional(readOnly = false)
	public void usePoint(Long userNo, BigDecimal userPointAmount) throws Exception {
		LocalDateTime nowDateTime = LocalDateTime.now();

		// 포인트 한도 확인
		BigDecimal userPoint = userPointRepository.getUserPoint(userNo);
		if (userPointAmount.compareTo(userPoint) > 0) {
			log.error("포인트 한도 초과 : 사용자 가용 포인트 {}, 사용요청 포인트 {}", userPoint, userPointAmount);
			throw new Exception("포인트 한도 초과");
		}

		// 사용자 사용가능 포인트 상세 내역 조회
		List<UserPointDetailUseInterface> userUsablePointList = userPointDetailRepository.getUserUsablePointList(userNo, nowDateTime.minus(Period.ofDays(370)));

		// 사용자 상세 포인트 내역 계산
		ArrayList<UserPointDetailUseInterface> useList = getUserPointUseInterface(userPointAmount, userUsablePointList, nowDateTime);

		log.info("포인트 차감 내역");
		useList.forEach(v -> log.info("PointDetailNo : {}, PointAmount : {}, ProcessDate : {}", v.getPointDetailId(), v.getPointAmount(), v.getProcessDate()));

		saveUserPoint(userNo, userPointAmount, nowDateTime, useList);
	}

	/**
	 * 사용자 포인트 객채 생성
	 */
	private UserPoint getUserPoint(Long userNo, BigDecimal parseChargePointAmount) {
		UserPoint userPoint = new UserPoint();

		userPoint.setPointAmount(parseChargePointAmount);
		userPoint.setPointStatus(PointStatus.CHARGE);
		userPoint.setProcessDate(LocalDateTime.now().withNano(0));
		userPoint.setUserNo(userNo);

		return userPoint;
	}

	/**
	 * 사용자 상세 포인트 내역 객체 생성
	 */
	private UserPointDetail getUserPointDetail(Long userNo, String userPointDetailId, BigDecimal parseChargePointAmount, UserPoint saveUserPoint) {
		UserPointDetail userPointDetail = new UserPointDetail();

		userPointDetail.setNo(userPointDetailId);
		userPointDetail.setPointDetailNo(userPointDetailId);
		userPointDetail.setPointAmount(parseChargePointAmount);
		userPointDetail.setPointStatus(PointStatus.CHARGE);
		userPointDetail.setProcessDate(LocalDateTime.now().withNano(0));
		userPointDetail.setUserNo(userNo);
		userPointDetail.setUserPoint(saveUserPoint);

		return userPointDetail;
	}

	/**
	 * 사용자 상세 포인트 내역 계산
	 */
	private ArrayList<UserPointDetailUseInterface> getUserPointUseInterface(BigDecimal userPointAmount, List<UserPointDetailUseInterface> userUsablePointList, LocalDateTime nowDateTime) {
		ArrayList<UserPointDetailUseInterface> useList = new ArrayList<>();
		BigDecimal usePoint = userPointAmount;

		for (UserPointDetailUseInterface userUsablePoint : userUsablePointList) {

			UserPointDetailUseInterface temp = getUserPointDetailUseInterfaceData(userUsablePoint);

			// 사용 포인트가 상세 내역의 포인트보다 클때 -> 상세 내역의 모든 포인트 소진
			if (usePoint.compareTo(userUsablePoint.getPointAmount()) > 0) {
				temp.setProcessDate(nowDateTime);
				temp.setPointAmount(userUsablePoint.getPointAmount().negate());
				useList.add(temp);
				usePoint = usePoint.subtract(userUsablePoint.getPointAmount());
			} else {
				// 사용 포인트가 상세 내역의 포인트 보다 작거나 같을때 -> 사용 포인트 금액으로 상세 내역 포인트 저장
				temp.setPointAmount(usePoint.negate());
				temp.setProcessDate(nowDateTime);
				useList.add(temp);
				break;
			}
		}

		return useList;
	}

	/**
	 * 사용자 포인트 사용 내역 저장
	 */
	@Transactional(readOnly = false)
	protected void saveUserPoint(Long userNo, BigDecimal userPointAmount, LocalDateTime nowDateTime, ArrayList<UserPointDetailUseInterface> useList) throws Exception {
		try {
			UserPoint useUserPoint = userPointRepository.save(getUserUsePoint(userNo, userPointAmount.negate(), nowDateTime.withNano(0)));

			List<UserPointDetail> userPointDetailList = getUserPointDetailList(userNo, useUserPoint, useList);
			userPointDetailList = userPointDetailRepository.saveAll(userPointDetailList);

			log.info("사용자 포인트 사용 내역");
			userPointDetailList.forEach(v -> log.info(v.toString()));

		} catch (Exception e) {
			log.error("사용자 포인트 내역 저장 실패 {}", e.getMessage());
			throw new Exception("사용자 포인트 내역 저장 실패");
		}
	}

	/**
	 * 사용자 포인트 상세 사용내역 생성
	 */
	protected List<UserPointDetail> getUserPointDetailList(Long userNo, UserPoint useUserPoint, ArrayList<UserPointDetailUseInterface> useList) {

		List<UserPointDetail> userPointDetailList;

		userPointDetailList = useList.stream()//
				.map(v -> new UserPointDetail(null, userNo, v.getPointDetailId(), v.getProcessDate(), PointStatus.USE, v.getPointAmount(), useUserPoint))//
				.collect(Collectors.toList());

		return userPointDetailList;
	}

	/**
	 * 사용자 포인트 사용정보 생성
	 */
	protected UserPoint getUserUsePoint(Long userNo, BigDecimal userPointAmount, LocalDateTime processDate) {
		UserPoint userPoint = new UserPoint();

		userPoint.setUserNo(userNo);
		userPoint.setPointAmount(userPointAmount);
		userPoint.setProcessDate(processDate);
		userPoint.setPointStatus(PointStatus.USE);

		return userPoint;
	}

	/**
	 * 계산 및 업데이트용 사용자 포인트 사용 객채 생성
	 */
	protected UserPointDetailUseInterface getUserPointDetailUseInterfaceData(UserPointDetailUseInterface userUsablePoint) {
		UserPointDetailUseInterface temp = new UserPointDetailUseInterface() {

			private String pointDetailId;
			private BigDecimal pointAmount;
			private LocalDateTime processDate;

			@Override
			public String getPointDetailId() {
				return pointDetailId;
			}

			@Override
			public void setPointDetailId(String pointDetailId) {
				this.pointDetailId = pointDetailId;
			}

			@Override
			public BigDecimal getPointAmount() {
				return pointAmount;
			}

			@Override
			public void setPointAmount(BigDecimal pointAmount) {
				this.pointAmount = pointAmount;
			}

			@Override
			public LocalDateTime getProcessDate() {
				return processDate;
			}

			@Override
			public void setProcessDate(LocalDateTime processDate) {
				this.processDate = processDate;
			}

		};

		temp.setPointDetailId(userUsablePoint.getPointDetailId());
		temp.setPointAmount(userUsablePoint.getPointAmount());
		temp.setProcessDate(userUsablePoint.getProcessDate());

		return temp;
	}

	/**
	 * 사용자 사용 포인트 내역 취소
	 */
	@Transactional(readOnly = false)
	public void usePointCancel(UserPoint userPoint) {

		// userPoint select
		userPoint = userPointRepository.getById(userPoint.getNo());

		// userPointDetail Table 취소 처리
		userPointDetailRepository.deleteAll(userPoint.getUserPointsDetail());

		// userPoint Table 취소 처리
		userPointRepository.delete(userPoint);
	}

	/**
	 * 사용자 포인트 만료
	 */
	@Transactional(readOnly = false)
	public void userPointExpired(Long userNo) {
		LocalDateTime expiredDate = LocalDateTime.now().minusDays(365);

		List<UserPoint> findAllExpiredUserPoint = userPointRepository.findExpiredUserPointList(userNo, expiredDate);

		log.info("사용자 NO {} : 포인트 만료 처리", userNo);
		findAllExpiredUserPoint.forEach(v -> {
			if (v != null) {
				log.info(v.toString());
			}
		});

		findAllExpiredUserPoint.forEach(v -> {
			UserPoint expiredUserPoint = new UserPoint(UuidGenerator.getUuid(), v.getUserNo(), LocalDateTime.now().withNano(0), PointStatus.EXPIRED, v.getPointAmount().negate(), new ArrayList<>());
			userPointRepository.save(expiredUserPoint);
			String userPointDetailId = UuidGenerator.getUuid();
			UserPointDetail userPointDetail = getUserPointDetail(v.getUserNo(), userPointDetailId, v.getPointAmount().negate(), v);
			userPointDetail.setPointStatus(PointStatus.EXPIRED);
			userPointDetailRepository.save(userPointDetail);
		});

	}

}
