package com.mymyeong.codetest.userpoint;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("사용자 포인트 API 테스트")
@ExtendWith(SpringExtension.class)
@WebMvcTest
class UserPointControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserPointService userPointService;

	@Test
	@DisplayName("사용자 포인트 조회")
	void testGetUserPoint() throws Exception {
		mockMvc.perform(get("/user-point/2"))//
				.andExpect(status().isOk())//
				.andExpect(content().string("0"));
	}

	@Test
	@DisplayName("사용자 포인트 사용 내역 조회")
	void testGetUserPointList() throws Exception {
		mockMvc.perform(get("/user-point/2/list"))//
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("사용자 포인트 사용")
	void testUsePoint() throws Exception {

		// 정상
		String content = "{\"usePointAmount\": \"500\"}";
		mockMvc.perform(post("/user-point/1/usePoint").contentType(MediaType.APPLICATION_JSON).content(content))//
				.andExpect(status().isCreated());

		// 사용포인트 미입력
		content = "{\"\": \"\"}";
		mockMvc.perform(post("/user-point/1/usePoint").contentType(MediaType.APPLICATION_JSON).content(content))//
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("사용자 포인트 적립")
	void testChargePoint() throws Exception {
		// 정상
		String content = "{\"chargePointAmount\": \"500\"}";
		mockMvc.perform(post("/user-point/1/chargePoint").contentType(MediaType.APPLICATION_JSON).content(content))//
				.andExpect(status().isCreated());

		// 사용포인트 미입력
		content = "{\"\": \"\"}";
		mockMvc.perform(post("/user-point/1/chargePoint").contentType(MediaType.APPLICATION_JSON).content(content))//
				.andExpect(status().isBadRequest());
	}

}
