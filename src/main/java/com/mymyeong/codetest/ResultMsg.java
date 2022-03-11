package com.mymyeong.codetest;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResultMsg {
	/** 응답 코드 */
	private String resultCode;

	/** 응답 메시지 */
	private String resultMsg;

	/** 응답 일자 */
	private LocalDateTime responseDateTime;
}
