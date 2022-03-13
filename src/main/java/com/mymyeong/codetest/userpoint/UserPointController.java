package com.mymyeong.codetest.userpoint;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	UserPointService userPointService;

	/**
	 * 사용자 포인트 조회
	 * 
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{userNo}")
	public Long getPoint(@PathVariable Long userNo) throws Exception {
		log.info("사용자 포인트 조회 : USER_NO[{}] ", userNo);
		return userPointService.getUserPoint(userNo);
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
	public List<UserPoint> getPointList(@PathVariable Long userNo, @PageableDefault(size = 5) Pageable pageable) throws Exception {

		log.info("사용자 포인트 적립/사용 내역 조회 : USER_NO[{}] ", userNo);

		return userPointService.getUserPointList(userNo, pageable);
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
	public List<UserPointDetailUseInterface> getUseablePointList(@PathVariable Long userNo, @PageableDefault(size = 5) Pageable pageable) throws Exception {

		log.info("사용자 가용 포인트 상세 내역 조회 : USER_NO[{}] ", userNo);

		return userPointService.getUseablePointList(userNo, pageable);
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
	public ResponseEntity<ResultMsg> chargePoint(@PathVariable Long userNo, @Valid @RequestBody HashMap<String, String> map) throws Exception {

		String chargePointAmount = map.get("chargePointAmount");

		log.info("사용자 포인트 충전 : USER_NO[{}], chargePointAmount[{}]", userNo, chargePointAmount);

		if (chargePointAmount == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "충전 포인트를 입력해 주세요");
		}

		Long parseChargePointAmount = Long.valueOf(chargePointAmount);

		userPointService.chargePoint(userNo, parseChargePointAmount);

		return new ResponseEntity<>(getSussceResultMsg(), HttpStatus.CREATED);
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
	public ResponseEntity<ResultMsg> usePoint(@PathVariable Long userNo, @Valid @RequestBody HashMap<String, String> map) throws Exception {

		String usePointAmount = map.get("usePointAmount");

		log.info("사용자 포인트 사용 : USER_NO[{}], usePointAmount[{}]", userNo, usePointAmount);

		if (usePointAmount == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용 포인트를 입력해 주세요");
		}

		Long paseLongUserPointAmount = Long.valueOf(usePointAmount);

		userPointService.usePoint(userNo, paseLongUserPointAmount);

		return new ResponseEntity<>(getSussceResultMsg(), HttpStatus.CREATED);
	}

	/**
	 * 사용자 포인트 내역 취소
	 * 
	 * @param userNo
	 * @param userPoint
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/{userNo}/usePointCancel")
	public ResponseEntity<ResultMsg> usePointCancel(@PathVariable Long userNo, @Valid @RequestBody UserPoint userPoint) throws Exception {

		log.info("사용자 포인트 사용 취소 : USER_NO[{}], userPoint[{}]", userNo, userPoint);

		userPointService.usePointCancel(userPoint);

		return new ResponseEntity<>(getSussceResultMsg(), HttpStatus.CREATED);
	}

	/**
	 * 사용자 포인트 만료 처리
	 * 
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/{userNo}/pointExpired")
	public ResponseEntity<ResultMsg> pointExpired(@PathVariable Long userNo) throws Exception {

		log.info("사용자 포인트 만료 처리 : USER_NO[{}], userPoint[{}]", userNo);

		userPointService.userPointExpired(userNo);

		return new ResponseEntity<>(getSussceResultMsg(), HttpStatus.CREATED);
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
