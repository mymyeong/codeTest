package com.mymyeong.musinsa.userpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-point")
public class UserPointController {

	@Autowired
	UserRepository userRepository;

	UserPointRepository userPointRepository;

	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/users/{userId}")
	public User retrieveAllUsers(@PathVariable Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@GetMapping("/users/{userId}/point")
	public List<UserPoint> retrieveUserPoint(@PathVariable Long userId) {
		return null;
	}
}
