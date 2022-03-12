package com.mymyeong.codetest.userpoint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("사용자 포인트 API 테스트")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserPointControllerTest {

    @Autowired
    MockMvc mockMvc;

//    @Autowired
//    UserPointController userPointController;

    @Test
    @DisplayName("사용자 포인트 조회")
    void testGetUserPoint() {
//	try {
//	    // given
//	    Long userNo = 1l;
//
//	    // when
//	    Long userPoint = userPointController.getUserPoint(userNo);
//
//	    // then
//	    assertEquals(userPoint, 1500L);
//	} catch (Exception e) {
//	    fail("사용자 포인트 조회 실패");
//	}
//
//	try {
//	    // given
//	    Long userNo = 2l;
//
//	    // when
//	    Long userPoint = userPointController.getUserPoint(userNo);
//
//	    // then
//	    assertEquals(userPoint, 0L);
//	} catch (Exception e) {
//	    fail("사용자 포인트 조회 실패");
//	}
    }

    @Test
    @DisplayName("사용자 포인트 사용 내역 조회")
    void testGetUserPointList() {
//	mockMvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(content().string("hello saelobi"))
//		.andDo(print());
    }

    @Test
    @DisplayName("사용자 포인트 사용가능 내역 조회")
    void testGetUserUseablePointList() {
    }

    @Test
    @DisplayName("사용자 포인트 사용")
    void testUsePoint() {
    }

    @Test
    @DisplayName("사용자 포인트 적립")
    void testChargePoint() {
    }

}
