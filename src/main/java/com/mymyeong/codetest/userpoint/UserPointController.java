package com.mymyeong.codetest.userpoint;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 포인트 Controller
 * 
 * @author mymyeong
 *
 */
@RestController
@RequestMapping("/user-point")
public class UserPointController {

	@Autowired
	UserPointRepository userPointRepository;

	/**
	 * 유저 포인트 조회
	 * 
	 * @param userNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("{userNo}/point")
	public Long getUserPoint(@PathVariable Long userNo) throws Exception {
		return userPointRepository.getUserPoint(userNo);
	}

	/**
	 * 유저 포인트 적립/사용 내역 조회
	 * 
	 * @param userNo
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/{userNo}/pointList")
	public List<UserPoint> getUserPointList(@PathVariable Long userNo, @PageableDefault(size = 5) Pageable pageable) throws Exception {

		Page<UserPoint> userPointList = userPointRepository.findAllByUserNoOrderByProcessDate(userNo, pageable);

		if (userPointList == null || userPointList.getSize() < 1) {
			return new ArrayList<>();
		}

		return userPointList.toList();
	}

	@PostMapping("/{userNo}")
	public Long usePoint(@PathVariable Long userNo, @Valid @RequestBody HashMap<String, String> map, @PageableDefault(size = 5) Pageable pageable) throws Exception {

		String usePointAmount = map.get("usePointAmount");

		if (usePointAmount == null) {
			throw new Exception("사용 포인트를 입력해 주세요");
		}

		Long paseLongUserPointAmount = Long.valueOf(usePointAmount);

		Long userPoint = userPointRepository.getUserPoint(userNo);
		if (userPoint < paseLongUserPointAmount) {
			throw new Exception("포인트 한도 초과");
		}

		LocalDateTime time = LocalDateTime.now().minus(Period.ofDays(368));

		Page<UserPointDetailUseInterface> userPointList = userPointRepository.getUserPointList(userNo, time, pageable);

		for (UserPointDetailUseInterface list : userPointList) {
//			Arrays.stream(list).forEach(v -> System.out.println(v));
			System.out.println(list.getPointAmount() + " / " + list.getProcessDate());
		}

		return null;
	}

}
