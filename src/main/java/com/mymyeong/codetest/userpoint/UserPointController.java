package com.mymyeong.codetest.userpoint;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mymyeong.codetest.ResultMsg;

import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 포인트 Controller
 * 
 * @author mymyeong
 *
 */
@RestController
@RequestMapping("/user-point")
@Slf4j
public class UserPointController {

	@Autowired
	UserPointRepository userPointRepository;

	@Autowired
	UserPointDetailRepository userPointDetailRepository;

	/**
	 * 사용자 포인트 조회
	 * 
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("{userNo}/")
	public Long getUserPoint(@PathVariable Long userNo) throws Exception {
		log.info("사용자 포인트 조회 : USER_NO[{}] ", userNo);
		return userPointRepository.getUserPoint(userNo);
	}

	/**
	 * 사용자 포인트 적립/사용 내역 조회
	 * 
	 * @param userNo
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{userNo}/list")
	public List<UserPoint> getUserPointList(@PathVariable Long userNo, @PageableDefault(size = 5) Pageable pageable) throws Exception {

		log.info("사용자 포인트 적립/사용 내역 조회 : USER_NO[{}] ", userNo);

		Page<UserPoint> userPointList = userPointRepository.findAllByUserNoOrderByProcessDate(userNo, pageable);

		if (userPointList == null || userPointList.getSize() < 1) {
			return new ArrayList<>();
		}

		return userPointList.toList();
	}

	/**
	 * 사용자 가용 포인트 상세 내역 조회
	 * 
	 * @param userNo
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{userNo}/useableList")
	public List<UserPointDetailUseInterface> getUserUseablePointList(@PathVariable Long userNo, @PageableDefault(size = 5) Pageable pageable) throws Exception {

		log.info("사용자 가용 포인트 상세 내역 조회 : USER_NO[{}] ", userNo);

		List<UserPointDetailUseInterface> userPointList = userPointDetailRepository.getUserUseablePointList(userNo, LocalDateTime.now().minus(Period.ofDays(370)));

		return userPointList;
	}

	/**
	 * 사용자 포인트 충전
	 * 
	 * @param userNo
	 * @param map    chargePointAmount : 충전 포인트 금액
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/{userNo}/chargePoint")
	public ResultMsg chargePoint(@PathVariable Long userNo, @Valid @RequestBody HashMap<String, String> map) throws Exception {

		String chargePointAmount = map.get("chargePointAmount");

		log.info("사용자 포인트 충전 : USER_NO[{}], chargePointAmount[{}]", userNo, chargePointAmount);

		if (chargePointAmount == null) {
			throw new Exception("충전 포인트를 입력해 주세요");
		}

		Long parseChargePointAmount = Long.valueOf(chargePointAmount);

		chargePoint(userNo, parseChargePointAmount);

		return getSussceResultMsg();
	}

	/**
	 * 사용자 포인트 사용
	 * 
	 * @param userNo
	 * @param map    usePointAmount : 포인트 사용 금액
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/{userNo}/usePoint")
	public ResultMsg usePoint(@PathVariable Long userNo, @Valid @RequestBody HashMap<String, String> map) throws Exception {

		String usePointAmount = map.get("usePointAmount");

		log.info("사용자 포인트 사용 : USER_NO[{}], usePointAmount[{}]", userNo, usePointAmount);

		if (usePointAmount == null) {
			throw new Exception("사용 포인트를 입력해 주세요");
		}

		Long paseLongUserPointAmount = Long.valueOf(usePointAmount);

		// 포인트 한도 확인
		Long userPoint = userPointRepository.getUserPoint(userNo);
		if (userPoint < paseLongUserPointAmount) {
			log.error("포인트 한도 초과 : 사용자 가용 포인트 {}, 사용요청 포인트 {}", userPoint, paseLongUserPointAmount);
			throw new Exception("포인트 한도 초과");
		}

		LocalDateTime nowDateTime = LocalDateTime.now();

		// 사용자 사용가능 포인트 상세 내역 조회
		List<UserPointDetailUseInterface> userUseablePointList = userPointDetailRepository.getUserUseablePointList(userNo, nowDateTime.minus(Period.ofDays(370)));

		// 사용자 상세 포인트 내역 계산
		ArrayList<UserPointDetailUseInterface> useList = getUserPointUseInterface(paseLongUserPointAmount, userUseablePointList, nowDateTime);

		log.info("포인트 차감 내역");
		useList.stream().forEach(v -> log.info("PointDetailNo : {}, PointAmount : {}, ProcessDate : {}", v.getPointDetailNo(), v.getPointAmount(), v.getProcessDate()));

		saveUserPoint(userNo, paseLongUserPointAmount, nowDateTime, useList);

		return getSussceResultMsg();
	}

	/**
	 * 포인트 충전
	 * 
	 * @param userNo
	 * @param parseChargePointAmount
	 * @throws Exception
	 */
	@Transactional
	protected void chargePoint(Long userNo, Long parseChargePointAmount) throws Exception {

		UserPoint userPoint = getUserPoint(userNo, parseChargePointAmount);
		UserPoint saveUserPoint = userPointRepository.save(userPoint);
		log.info("save UserPoint : {}", saveUserPoint);
		Long userPointDetailSeq = userPointDetailRepository.getUserPointDetailSeq();
		UserPointDetail userPointDetail = getUserPointDetail(userNo, userPointDetailSeq, parseChargePointAmount, saveUserPoint);
		log.info("save userPointDetail : {}", userPointDetail);
		Integer saveUserPointDetail = userPointDetailRepository.saveUserPointDetail(userPointDetail);

		if (saveUserPointDetail == null || saveUserPointDetail != 1) {
			throw new Exception();
		}
	}

	/**
	 * 사용자 포인트 객채 생성
	 * 
	 * @param userNo
	 * @param parseChargePointAmount
	 * @return
	 */
	private UserPoint getUserPoint(Long userNo, Long parseChargePointAmount) {
		UserPoint userPoint = new UserPoint();

		userPoint.setPointAmount(parseChargePointAmount);
		userPoint.setPointStatus(PointStatus.CHARGE);
		userPoint.setProcessDate(LocalDateTime.now().withNano(0));
		userPoint.setUserNo(userNo);

		return userPoint;
	}

	/**
	 * 사용자 상세 포인트 내역 객체 생성
	 * 
	 * @param userNo
	 * @param parseChargePointAmount
	 * @param parseChargePointAmount2
	 * @param saveUserPoint
	 * @return
	 */
	private UserPointDetail getUserPointDetail(Long userNo, Long userPointDetailSeq, Long parseChargePointAmount, UserPoint saveUserPoint) {
		UserPointDetail userPointDetail = new UserPointDetail();

		userPointDetail.setNo(userPointDetailSeq);
		userPointDetail.setPointDetailNo(userPointDetailSeq);
		userPointDetail.setPointAmount(parseChargePointAmount);
		userPointDetail.setPointStatus(PointStatus.CHARGE);
		userPointDetail.setProcessDate(LocalDateTime.now().withNano(0));
		userPointDetail.setUserNo(userNo);
		userPointDetail.setUserPoint(saveUserPoint);

		return userPointDetail;
	}

	/**
	 * 사용자 상세 포인트 내역 계산
	 * 
	 * @param paseLongUserPointAmount
	 * @param userUseablePointList
	 * @param nowDateTime
	 * @return
	 */
	private ArrayList<UserPointDetailUseInterface> getUserPointUseInterface(Long paseLongUserPointAmount, List<UserPointDetailUseInterface> userUseablePointList, LocalDateTime nowDateTime) {
		ArrayList<UserPointDetailUseInterface> useList = new ArrayList<UserPointDetailUseInterface>();
		Long usePoint = paseLongUserPointAmount;

		for (UserPointDetailUseInterface userUseablePoint : userUseablePointList) {

			UserPointDetailUseInterface temp = getUserPointDetailUseInterfaceData(userUseablePoint);

			// 사용 포인트가 상세 내역의 포인트보다 클때 -> 상세 내역의 모든 포인트 소진
			if (usePoint > userUseablePoint.getPointAmount()) {
				temp.setProcessDate(nowDateTime);
				temp.setPointAmount(-userUseablePoint.getPointAmount());
				useList.add(temp);
				usePoint -= userUseablePoint.getPointAmount();
			} else {
				// 사용 포인트가 상세 내역의 포인트 보다 작거나 같을때 -> 사용 포인트 금액으로 상세 내역 포인트 저장
				temp.setPointAmount(-usePoint);
				temp.setProcessDate(nowDateTime);
				useList.add(temp);
				break;
			}
		}

		return useList;
	}

	/**
	 * 사용자 포인트 사용 내역 저장
	 * 
	 * @param userNo
	 * @param paseLongUserPointAmount
	 * @param nowDateTime
	 * @param useList
	 * @throws Exception
	 */
	@Transactional
	protected void saveUserPoint(Long userNo, Long paseLongUserPointAmount, LocalDateTime nowDateTime, ArrayList<UserPointDetailUseInterface> useList) throws Exception {
		try {
			UserPoint useUserPoint = userPointRepository.save(getUserUsePoint(userNo, -paseLongUserPointAmount, nowDateTime.withNano(0)));
			log.debug("useUserPoint : {}" + useUserPoint);

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
	 * 
	 * @param userNo
	 * @param useUserPoint
	 * @param useList
	 * @return
	 */
	protected List<UserPointDetail> getUserPointDetailList(Long userNo, UserPoint useUserPoint, ArrayList<UserPointDetailUseInterface> useList) {

		List<UserPointDetail> userPointDetailList = new ArrayList<UserPointDetail>();

		userPointDetailList = useList.stream()//
				.map(v -> new UserPointDetail(null, userNo, v.getPointDetailNo(), v.getProcessDate(), PointStatus.USE, v.getPointAmount(), useUserPoint))//
				.collect(Collectors.toList());

		return userPointDetailList;
	}

	/**
	 * 사용자 포인트 사용정보 생성
	 * 
	 * @param userNo
	 * @param paseLongUserPointAmount
	 * @param processDate
	 * @return
	 */
	protected UserPoint getUserUsePoint(Long userNo, Long paseLongUserPointAmount, LocalDateTime processDate) {
		UserPoint userPoint = new UserPoint();

		userPoint.setUserNo(userNo);
		userPoint.setPointAmount(paseLongUserPointAmount);
		userPoint.setProcessDate(processDate);
		userPoint.setPointStatus(PointStatus.USE);

		return userPoint;
	}

	/**
	 * 계산 및 업데이트용 사용자 포인트 사용 객채 생성
	 * 
	 * @param userUseablePoint
	 * @return
	 */
	protected UserPointDetailUseInterface getUserPointDetailUseInterfaceData(UserPointDetailUseInterface userUseablePoint) {
		UserPointDetailUseInterface temp = new UserPointDetailUseInterface() {

			private Long pointDetailNo;
			private Long pointAmount;
			private LocalDateTime processDate;

			@Override
			public Long getPointDetailNo() {
				return pointDetailNo;
			}

			@Override
			public void setPointDetailNo(Long pointDetailNo) {
				this.pointDetailNo = pointDetailNo;
			}

			@Override
			public Long getPointAmount() {
				return pointAmount;
			}

			@Override
			public void setPointAmount(Long pointAmount) {
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

		temp.setPointDetailNo(userUseablePoint.getPointDetailNo());
		temp.setPointAmount(userUseablePoint.getPointAmount());
		temp.setProcessDate(userUseablePoint.getProcessDate());

		return temp;
	}

	/**
	 * 성공 메시지 생성
	 * 
	 * @return
	 */
	private ResultMsg getSussceResultMsg() {
		ResultMsg resultMsg = new ResultMsg("0", "성공", LocalDateTime.now());
		return resultMsg;
	}
}
