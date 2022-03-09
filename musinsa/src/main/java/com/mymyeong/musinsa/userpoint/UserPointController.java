package com.mymyeong.musinsa.userpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-point")
public class UserPointController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserPointRepository userPointRepository;

	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/users/{userNo}")
	public User retrieveAllUsers(@PathVariable Long userNo) {
		return userRepository.findById(userNo).orElse(null);
	}

	@GetMapping("/users/{userNo}/point")
	public List<UserPoint> retrieveUserPoint(@PathVariable Long userNo, Pageable pageable) throws Exception {
		User user = userRepository.findById(userNo).orElse(null);

		if (user == null) {
			throw new Exception("존재하지 않는 사용자 NO 입니다.");
		}

		return userPointRepository.findByUserOrderByEarnDateDesc(user, pageable).toList();
	}

}
